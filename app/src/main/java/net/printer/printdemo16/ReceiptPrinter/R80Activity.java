package net.printer.printdemo16.ReceiptPrinter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.TaskCallback;
import net.posprinter.utils.BitmapProcess;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.StringUtils;
import net.printer.printdemo16.MainActivity;
import net.printer.printdemo16.R;

import java.util.ArrayList;
import java.util.List;

public class R80Activity extends AppCompatActivity implements View.OnClickListener {

    private Button sample,text,barcode,qrcode,bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r80);
        initview();
    }

    private void initview(){
        sample = findViewById(R.id.bt_rcp);
        text = findViewById(R.id.bt_80text);
        barcode = findViewById(R.id.bt_80barcode);
        qrcode = findViewById(R.id.bt_80qr);
        bitmap = findViewById(R.id.bt_80bitmap);

        sample.setOnClickListener(this);
        text.setOnClickListener(this);
        barcode.setOnClickListener(this);
        qrcode.setOnClickListener(this);
        bitmap.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id==R.id.bt_rcp){
            printSample();
        }

        if(id== R.id.bt_80text){
            printText();
        }

        if (id== R.id.bt_80barcode){
            printBarcode();
        }

        if (id== R.id.bt_80qr){
            printqr();
        }

        if(id== R.id.bt_80bitmap){
            printBitmap();

        }
    }

    /**
     * 打印样张
     */
    private void printSample(){
        if (MainActivity.ISCONNECT){
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_success),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_failed),Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));//设置初始位置
                    list.add(DataForSendToPrinterPos80.selectCharacterSize(17));//字体放大一倍
                    list.add(StringUtils.strTobytes("商品"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("价格"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("5元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡呀"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("6元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("7元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("8元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("9元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(60,00));
                    list.add(StringUtils.strTobytes("黄焖鸡"));
                    list.add(DataForSendToPrinterPos80.setAbsolutePrintPosition(100,01));
                    list.add(StringUtils.strTobytes("10元"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());

                    return list;
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打印文本
     */
    private void printText(){

        if (MainActivity.ISCONNECT){
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_success),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_failed),Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    return list;
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     打印一维条码
     */
    private void printBarcode(){
        if (MainActivity.ISCONNECT){
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_success),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_failed),Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    //初始化打印机，清除缓存
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    //选择对齐方式
                    list.add(DataForSendToPrinterPos80.selectAlignment(1));
                    //选择HRI文字文字
                    list.add(DataForSendToPrinterPos80.selectHRICharacterPrintPosition(02));
                    //设置条码宽度
                    list.add(DataForSendToPrinterPos80.setBarcodeWidth(2));
                    //设置高度
                    list.add(DataForSendToPrinterPos80.setBarcodeHeight(80));
                    //条码的类型和内容，73是code128的类型，请参考说明手册每种类型的规则
                    list.add(DataForSendToPrinterPos80.printBarcode(73,10,"{B12345678"));
                    //打印指令
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    return list;
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 打印二维条码
     */
    private void printqr(){
        if (MainActivity.ISCONNECT){
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_success),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_failed),Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    //初始化打印机，清除缓存
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    //选择对齐方式
                    list.add(DataForSendToPrinterPos80.selectAlignment(1));
                    list.add(DataForSendToPrinterPos80.printQRcode(3,48,"www.baidu.com"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    return list;
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }

    private void printBitmap(){
//        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        final Bitmap bitmap1 =  BitmapProcess.compressBmpByYourWidth
                (BitmapFactory.decodeResource(getResources(), R.drawable.test_z),300);

        if (MainActivity.ISCONNECT){
            MainActivity.myBinder.WriteSendData(new TaskCallback() {
                @Override
                public void OnSucceed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_success),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnFailed() {
                    Toast.makeText(getApplicationContext(),getString(R.string.con_failed),Toast.LENGTH_SHORT).show();
                }
            }, new ProcessData() {
                @Override
                public List<byte[]> processDataBeforeSend() {
                    List<byte[]> list = new ArrayList<>();
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    List<Bitmap> blist= new ArrayList<>();
                    blist = BitmapProcess.cutBitmap(150,bitmap1);
                    for (int i= 0 ;i<blist.size();i++){
                        list.add(DataForSendToPrinterPos80.printRasterBmp(0,blist.get(i), BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Center,576));
                    }
//                    list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"));
                    list.add(DataForSendToPrinterPos80.printAndFeedLine());
                    return list;
                }
            });
        }else {
            Toast.makeText(getApplicationContext(),getString(R.string.connect_first),Toast.LENGTH_SHORT).show();
        }
    }
}
