package net.printer.printdemo16;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.utils.BitmapProcess;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterTSC;
import net.posprinter.utils.RoundQueue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TscActivity extends AppCompatActivity implements View.OnClickListener{

    private Button content,text,barcode,qrcode,bitmap;

    public static final int STATUS_READY = 0;
    public static final int STATUS_CARRIAGE_OPEN = 1;
    public static final int STATUS_PAPER_JAM = 2;
    public static final int STATUS_PAPER_EMPTY = 3;
    public static final int STATUS_RIBBON_ERROR = 4;
    public static final int STATUS_PAUSE = 5;
    public static final int STATUS_PRINTING = 6;
    public static final int STATUS_OTHER_ERROR = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tsc);
        initView();
    }

    private void initView(){
        content = findViewById(R.id.bt_tsp);
        text = findViewById(R.id.bt_text);
        barcode =findViewById(R.id.bt_barcode);
        qrcode = findViewById(R.id.bt_qr);
        bitmap = findViewById(R.id.bt_bitmap);

        content.setOnClickListener(this);
        text.setOnClickListener(this);
        barcode.setOnClickListener(this);
        qrcode.setOnClickListener(this);
        bitmap.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bt_tsp:
                printContent();
                break;
            case R.id.bt_text:
                printText();
                break;
            case   R.id.bt_barcode:
                printBarcode();
                break;
            case R.id.bt_qr:
                printQR();
                break;
            case R.id.bt_bitmap:
                printbitmap();
                break;
        }

    }


    //打印文本
    private void printContent(){
        if (MainActivity.ISCONNECT){

            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_success),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_failed),Toast.LENGTH_SHORT).show();

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    //设置标签纸大小
                    list.add(DataForSendToPrinterTSC.sizeBymm(50,30));
                    //设置间隙
                    list.add(DataForSendToPrinterTSC.gapBymm(2,0));
                    //清除缓存
                    list.add(DataForSendToPrinterTSC.cls());
                    //设置方向
                    list.add(DataForSendToPrinterTSC.direction(0));
                    //线条
                    list.add(DataForSendToPrinterTSC.bar(10,10,200,3));
                    //条码
                    list.add(DataForSendToPrinterTSC.barCode(10,45,"128",100,1,0,2,2,"abcdef12345"));
                    //文本,简体中文是TSS24.BF2,可参考编程手册中字体的代号
                    list.add(DataForSendToPrinterTSC.text(220,10,"TSS24.BF2",0,1,1,"这是测试文本"));
                    //打印
                    list.add(DataForSendToPrinterTSC.print(1));

                    return list;
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    //打印文本
    private void printText(){

        if (MainActivity.ISCONNECT){

            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void OnSucceed() {
                    /* 打开查询功能 */
                    String msg = "";
                    MainActivity.myBinder.openImmFunc();

                    while (true) {

                        int status = MainActivity.myBinder.queryStatus();

                        /* 如果是正在打印, 100ms后再查询 */
                        if (status ==STATUS_PRINTING) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }

                        if (status == STATUS_READY)
                            msg = "打印成功";
                        else if (status == STATUS_CARRIAGE_OPEN)
                            msg = "外盖未关";
                        else if (status == STATUS_PAPER_EMPTY)
                            msg = "缺纸";
                        else if (status == STATUS_PAPER_JAM)
                            msg = "卡纸";
                        else if (status == STATUS_RIBBON_ERROR)
                            msg = "碳带错误";
                        else if (status == STATUS_PAUSE)
                            msg = "打印暂停";
                        else if (status == STATUS_OTHER_ERROR)
                            msg = "其他错误";

                        break;
                    }

                    moveTaskToBack(true);
                    finishAndRemoveTask();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_failed),Toast.LENGTH_SHORT).show();

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    ArrayList<byte[]> list=new ArrayList<byte[]>();

                    int sendTotalCount = 10;
                    for(int i=0; i<sendTotalCount; i++ ) {
                        list.add(DataForSendToPrinterTSC.sizeBymm(100,200));
                        list.add(DataForSendToPrinterTSC.speed(5));
                        list.add(DataForSendToPrinterTSC.density(10));
                        list.add(DataForSendToPrinterTSC.gapBymm(3,0));
                        list.add(DataForSendToPrinterTSC.cls());
                        list.add(DataForSendToPrinterTSC.codePage("UTF-8"));
                        list.add(DataForSendToPrinterTSC.text(785,107,"H2GPRM.TTF",90,9,9,"103"));
                        list.add(DataForSendToPrinterTSC.text(763,27,"H2GPRM.TTF",90,8,8,"본사(로젠)"));
                        list.add(DataForSendToPrinterTSC.text(741,27,"H2GPRM.TTF",90,8,8,"선불 2,500"));
                        list.add(DataForSendToPrinterTSC.text(653,27,"H2GPRM.TTF",90,8,8,"10310000"));
                        list.add(DataForSendToPrinterTSC.text(763,257,"H2GPRM.TTF",90,38,38,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(761,257,"H2GPRM.TTF",90,38,38,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(763,259,"H2GPRM.TTF",90,38,38,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(763,261,"H2GPRM.TTF",90,38,38,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(665,267,"H2GPRM.TTF",90,15,15,"934-8151-4280"));
                        list.add(DataForSendToPrinterTSC.barCode(625,267,"128",110,0,90,3,3,"93481514280"));
                        list.add(DataForSendToPrinterTSC.text(495,27,"H2GPRM.TTF",90,9,9,"올리브it** 010-1111-2222"));
                        list.add(DataForSendToPrinterTSC.text(471,27,"H2GPRM.TTF",90,9,9,"경기 부천시 정주로 53"));
                        list.add(DataForSendToPrinterTSC.text(447,27,"H2GPRM.TTF",90,9,9,"더퍼스트지식산업센터807호"));
                        list.add(DataForSendToPrinterTSC.text(415,107,"H2GPRM.TTF",90,9,9,"2021-09-09"));
                        list.add(DataForSendToPrinterTSC.text(415,437,"H2GPRM.TTF",90,9,9,"2021-09-09"));
                        list.add(DataForSendToPrinterTSC.text(345,27,"H2GPRM.TTF",90,9,9,"테스트 02-3415-8947"));
                        list.add(DataForSendToPrinterTSC.text(319,27,"H2GPRM.TTF",90,9,9,"서울 용산구 한강로3가"));
                        list.add(DataForSendToPrinterTSC.text(293,27,"H2GPRM.TTF",90,9,9,"삼구빌딩"));
                        list.add(DataForSendToPrinterTSC.text(215,282,"H2GPRM.TTF",90,15,15,"934-8151-4280"));
                        list.add(DataForSendToPrinterTSC.text(142,27,"H2GPRM.TTF",90,9,9,"111111"));
                        list.add(DataForSendToPrinterTSC.text(158,344,"H2GPRM.TTF",90,15,15,"1"));
                        list.add(DataForSendToPrinterTSC.text(85,247,"H2GPRM.TTF",90,16,16,"[정주로]"));
                        list.add(DataForSendToPrinterTSC.text(166,547,"H2GPRM.TTF",90,9,9,"선불"));
                        list.add(DataForSendToPrinterTSC.text(142,497,"H2GPRM.TTF",90,9,9,"2,500"));
                        list.add(DataForSendToPrinterTSC.text(785,887,"H2GPRM.TTF",90,9,9,"올리브it시스템"));
                        list.add(DataForSendToPrinterTSC.text(761,767,"H2GPRM.TTF",90,9,9,"010-1111-2222"));
                        list.add(DataForSendToPrinterTSC.text(713,767,"H2GPRM.TTF",90,9,9,"경기 부천시 정주로 53"));
                        list.add(DataForSendToPrinterTSC.text(689,767,"H2GPRM.TTF",90,9,9,"더퍼스트지식산업센터807호"));
                        list.add(DataForSendToPrinterTSC.text(665,767,"H2GPRM.TTF",90,9,9,"업체코드 10320016"));
                        list.add(DataForSendToPrinterTSC.text(615,907,"H2GPRM.TTF",90,9,9,"선불 2,500"));
                        list.add(DataForSendToPrinterTSC.text(755,1327,"H2GPRM.TTF",90,9,9,"2021-09-09"));
                        list.add(DataForSendToPrinterTSC.text(725,1327,"H2GPRM.TTF",90,9,9,"2021-09-09"));
                        list.add(DataForSendToPrinterTSC.text(685,1462,"H2GPRM.TTF",90,13,13,"1"));
                        list.add(DataForSendToPrinterTSC.text(645,1127,"H2GPRM.TTF",90,15,15,"934-8151-4280"));
                        list.add(DataForSendToPrinterTSC.barCode(605,1127,"128",195,0,90,3,3,"93481514280"));
                        list.add(DataForSendToPrinterTSC.text(400,1127,"H2GPRM.TTF",90,13,13,"934-8151-4280"));
                        list.add(DataForSendToPrinterTSC.text(435,787,"H2GPRM.TTF",90,29,29,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(435,787,"H2GPRM.TTF",90,29,29,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(433,787,"H2GPRM.TTF",90,29,29,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(435,789,"H2GPRM.TTF",90,29,29,"E5-330"));
                        list.add(DataForSendToPrinterTSC.text(356,887,"H2GPRM.TTF",90,9,9,"올리브it시스템 010-1111-2222"));
                        list.add(DataForSendToPrinterTSC.text(332,767,"H2GPRM.TTF",90,9,9,"경기 부천시 정주로 53"));
                        list.add(DataForSendToPrinterTSC.text(308,767,"H2GPRM.TTF",90,9,9,"더퍼스트지식산업센터807호"));
                        list.add(DataForSendToPrinterTSC.text(356,1492,"H2GPRM.TTF",90,9,9,"선불"));
                        list.add(DataForSendToPrinterTSC.text(332,1447,"H2GPRM.TTF",90,9,9,"2,500"));
                        list.add(DataForSendToPrinterTSC.text(253,887,"H2GPRM.TTF",90,9,9,"테스트 02-3415-8947"));
                        list.add(DataForSendToPrinterTSC.text(224,862,"H2GPRM.TTF",90,9,9,"본사기본 010-3415-8947"));
                        list.add(DataForSendToPrinterTSC.text(253,1357,"H2GPRM.TTF",90,9,9,"10320016"));
                        list.add(DataForSendToPrinterTSC.text(224,1357,"H2GPRM.TTF",90,9,9,"2021-09-09"));
                        list.add(DataForSendToPrinterTSC.text(128,1197,"H2GPRM.TTF",90,9,9,"111111"));
                        list.add(DataForSendToPrinterTSC.text(128,1492,"H2GPRM.TTF",90,15,15,"1"));
                        list.add(DataForSendToPrinterTSC.text(55,1432,"H2GPRM.TTF",90,16,16,"No.1"));
                        list.add(DataForSendToPrinterTSC.print(1,1));
                    }

                    return list;
                    //List<byte[]> list = new ArrayList<>();
                    //设置标签纸大小
                   // list.add(DataForSendToPrinterTSC.sizeBymm(50,30));
                    //设置间隙
                    //list.add(DataForSendToPrinterTSC.gapBymm(2,0));
                    //清除缓存
                    //list.add(DataForSendToPrinterTSC.cls());
                    //设置方向
                    //list.add(DataForSendToPrinterTSC.direction(0));
                    //线条
//                    list.add(DataForSendToPrinterTSC.bar(10,10,200,3));
                    //条码
//                    list.add(DataForSendToPrinterTSC.barCode(10,15,"128",100,1,0,2,2,"abcdef12345"));
                    //文本
                    //list.add(DataForSendToPrinterTSC.text(10,30,"TSS24.BF2",0,1,1,"abcasdjknf"));
                    //打印
                    //list.add(DataForSendToPrinterTSC.print(1));

                    //return list;
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }

    }

    private void printBarcode(){
        if (MainActivity.ISCONNECT){

            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_success),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_failed),Toast.LENGTH_SHORT).show();

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    //设置标签纸大小
                    list.add(DataForSendToPrinterTSC.sizeBymm(50,30));
                    //设置间隙
                    list.add(DataForSendToPrinterTSC.gapBymm(2,0));
                    //清除缓存
                    list.add(DataForSendToPrinterTSC.cls());
                    //设置方向
                    list.add(DataForSendToPrinterTSC.direction(0));
                    //线条
//                    list.add(DataForSendToPrinterTSC.bar(10,10,200,3));
                    //条码
                    list.add(DataForSendToPrinterTSC.barCode(10,15,"128",100,1,0,2,2,"abcdef12345"));
                    //文本
//                    list.add(DataForSendToPrinterTSC.text(10,30,"1",0,1,1,"abcasdjknf"));
                    //打印
                    list.add(DataForSendToPrinterTSC.print(1));

                    return list;
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    private void printQR(){
        if (MainActivity.ISCONNECT){

            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_success),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.send_failed),Toast.LENGTH_SHORT).show();

                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    //设置标签纸大小
                    list.add(DataForSendToPrinterTSC.sizeBymm(50,30));
                    //设置间隙
                    list.add(DataForSendToPrinterTSC.gapBymm(2,0));
                    //清除缓存
                    list.add(DataForSendToPrinterTSC.cls());
                    //设置方向
                    list.add(DataForSendToPrinterTSC.direction(0));
                    //具体参数值请参看编程手册
                    list.add(DataForSendToPrinterTSC.qrCode(10,20,"M",3,"A",0,"M1","S3","123456789"));
                    //打印
                    list.add(DataForSendToPrinterTSC.print(1));

                    return list;
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    private void printbitmap(){
        final Bitmap bitmap1 =  BitmapProcess.compressBmpByYourWidth
                (BitmapFactory.decodeResource(getResources(), R.drawable.test_z),150);
        MainActivity.myBinder.WriteSendData(new TaskCallback() {
            @Override
            public void OnSucceed() {
                Toast.makeText(getApplicationContext(),getString(R.string.send_success),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OnFailed() {
                Toast.makeText(getApplicationContext(),getString(R.string.send_failed),Toast.LENGTH_SHORT).show();

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {

                List<byte[]> list = new ArrayList<>();
                //设置标签纸大小
                list.add(DataForSendToPrinterTSC.sizeBymm(50,30));
                //设置间隙
                list.add(DataForSendToPrinterTSC.gapBymm(2,0));
                //清除缓存
                list.add(DataForSendToPrinterTSC.cls());
                list.add(DataForSendToPrinterTSC.bitmap(10,10,0,bitmap1, BitmapToByteData.BmpType.Threshold));
                list.add(DataForSendToPrinterTSC.print(1));

                return list;
            }
        });
    }

}
