package com.aboust.develop_guide.ui.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.aboust.develop_guide.R
import kotlinx.android.synthetic.main.activity_qr_code.*
import timber.log.Timber


class CodeScanActivity : BaseActivity(), QRCodeView.Delegate {
    companion object {
        private const val REQUEST_CODE_CHOOSE_QR_CODE_FROM_GALLERY = 0x10090
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_qr_code)


        qr_code_view.setDelegate(this)
    }
//
//    /**
//     * 用户点击拨打电话按钮，先进行申请权限
//     */
//    private fun requestPermission(context: Context) {
//        // 判断是否需要运行时申请权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                && ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // 判断是否需要对用户进行提醒，用户点击过拒绝&&没有勾选不再提醒时进行提示
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//                // 给用于予以权限解释, 对于已经拒绝过的情况，先提示申请理由，再进行申请
//                showPermissionRationale("需要打开电话权限直接进行拨打电话，方便您的操作")
//            } else {
//                // 无需说明理由的情况下，直接进行申请。如第一次使用该功能（第一次申请权限），用户拒绝权限并勾选了不再提醒
//                // 将引导跳转设置操作放在请求结果回调中处理
//                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PERMISSION_CODE_CALL_PHONE)
//            }
//        } else {
//            // 拥有权限直接进行功能调用
//            callPhone()
//        }
//    }
//
//
//    /**
//     * 权限申请回调
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        // 根据requestCode判断是那个权限请求的回调
//        if (requestCode == REQUEST_PERMISSION_CODE_CALL_PHONE) {
//            // 判断用户是否同意了请求
//            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                callPhone()
//            } else {
//                // 未同意的情况
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//                    // 给用于予以权限解释, 对于已经拒绝过的情况，先提示申请理由，再进行申请
//                    showPermissionRationale("需要打开电话权限直接进行拨打电话，方便您的操作")
//                } else {
//                    // 用户勾选了不再提醒，引导用户进入设置界面进行开启权限
//                    Snackbar.make(view, "需要打开权限才能使用该功能，您也可以前往设置->应用。。。开启权限",
//                            Snackbar.LENGTH_INDEFINITE)
//                            .setAction("确定") {
//                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                intent.data = Uri.parse("package:$packageName")
//                                startActivityForResult(intent, REQUEST_SETTINGS_CODE)
//                            }
//                            .show()
//                }
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_SETTINGS_CODE) {
//            Toast.makeText(this, "再次判断是否同意了权限，再进行自定义处理",
//                    Toast.LENGTH_LONG).show()
//        }
//    }


    override fun onStart() {
        super.onStart()
        qr_code_view.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
//        qr_code_view.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT) // 打开前置摄像头开始预览，但是并未开始识别
        qr_code_view.startSpotAndShowRect() // 显示扫描框，并开始识别


    }

    override fun onStop() {
        qr_code_view.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        qr_code_view.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    override fun onScanQRCodeSuccess(result: String) {
        Timber.i("result:$result")
        title = "扫描结果为：$result"
        vibrate()
        qr_code_view.startSpot() // 开始识别
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText: String = qr_code_view.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                qr_code_view.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                qr_code_view.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Timber.e("打开相机出错")
    }

    /**
     * 选qrcode图片
     */
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        qr_code_view.showScanRect()
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QR_CODE_FROM_GALLERY) {
//            val picturePath: String = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0)
//            qr_code_view.decodeQRCode(picturePath)
//        }
//    }
}