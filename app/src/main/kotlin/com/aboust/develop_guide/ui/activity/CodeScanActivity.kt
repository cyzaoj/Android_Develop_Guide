package com.aboust.develop_guide.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
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