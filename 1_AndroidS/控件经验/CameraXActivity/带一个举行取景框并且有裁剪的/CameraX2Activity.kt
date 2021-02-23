package com.teetaa.outsourcing.carinspection.activity.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_90
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.teetaa.outsourcing.carinspection.R
import com.teetaa.outsourcing.carinspection.activity.SuperActivity
import com.teetaa.outsourcing.carinspection.activity.ordertaking.OrderTakingActivity
import com.teetaa.outsourcing.carinspection.thirdpart.gilde.GlideUtils
import com.teetaa.outsourcing.carinspection.util.ClickEventHandler
import com.teetaa.outsourcing.carinspection.util.ViewPositionAndSizeGetter
import com.teetaa.outsourcing.carinspection.util.setClickEventHandler
import com.teetaa.outsourcing.carinspection.widgets.LoadingDiaglog
import kotlinx.android.synthetic.main.activity_camera_x2.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 带一个矩形取景框,
 */
class CameraX2Activity : SuperActivity() {
    private val loading by lazy {
        LoadingDiaglog()
    }
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private var _filePath: String? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    override fun getSubTag(): String {
        return "CameraX2Activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_x2)
    }

    override fun firstFocusChanged() {
        outputDirectory = getOutputDirectory(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera()
        camerax_capture_button.setTag(ClickEventHandler.instance.TAG_CAMERA_ALBUM, 1)
        camerax_capture_button.setClickEventHandler {
            takePhoto()
        }

    }


    //准备相机
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            preview = Preview.Builder()
                .build()
            /*cyan 这里的属性我发现在某些手机上不一定起效，但是这种部分是有效果的*/
            imageCapture = ImageCapture.Builder().setTargetRotation(ROTATION_90)
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
            // Select back camera
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                ///createSurfaceProvider(camera?.cameraInfo)
                preview?.setSurfaceProvider(camerax_viewFinder.surfaceProvider)

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    fun takePhoto() {
        loading.showDialog(this@CameraX2Activity)
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT).format(System.currentTimeMillis()) + ".hn_xzh_cj.png"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()
        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    loading.hideDialog()
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    File(photoFile.absolutePath)?.also { file ->
                        if (file.exists()) {
                            val savedUri = Uri.fromFile(file)//Uri.fromFile(photoFile)

                            _filePath = file.absolutePath

//                            var options: BitmapFactory.Options? = BitmapFactory.Options()
//                            options!!.inJustDecodeBounds = false
//                            options.inSampleSize = 4 // 图片的大小设置为原来的8分之一
//                            val bmp = BitmapFactory.decodeFile(file.absolutePath, options)
//                            options = null
//                            loading.hideDialog()
//                            displayThePhoto(bmp)

                            returnRotatePhoto(file.absolutePath)?.also {
                                displayThePhoto(it)
                            }

                            val msg =
                                "Photo capture succeeded: $savedUri,File is Exist=${file.absolutePath}"
                            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            Log.d(TAG, msg)
                        } else {
                            loading.hideDialog()
                            Log.d(TAG, "file not exist!!!!")
                        }
                    }

                }
            })
    }

    private fun displayThePhoto(bm: Bitmap) {
//        returnRotatePhoto(file.absolutePath)?.also { bitmap ->
//
//        }
        camerax_preview_photo.also { displayView ->
            //cyan 先把原始图片放在imageview上，但是不显示出来，然后通过这个imageview来做截取
            GlideUtils.loadBitMapImage(
                this,
                bm,
                R.drawable.coner_button_disable_bg2,
                R.drawable.coner_button_disable_bg2,
                displayView
            )

            //
            mHandler.postDelayed({
                //cyan开始通过取景框的大小来截取一个图片，获取取景框的位置和大小信息
                val res = ViewPositionAndSizeGetter().calculate(
                    signCenterArea,
                    this@CameraX2Activity,
                    this@CameraX2Activity
                )
                //cyan获取上面的说的放了图片的imageview上面的bitmap，然后截取一部分
                val image: Bitmap =
                    (camerax_preview_photo.getDrawable() as BitmapDrawable).getBitmap()
                val matrix = Matrix()
                val returnBm =
                    Bitmap.createBitmap(
                        image,
                        res.x,
                        res.y,
                        res.w,
                        res.h,
                        matrix,
                        true
                    )
                //把这个截取后的部分显示出来
                GlideUtils.loadBitMapImage(
                    this,
                    returnBm,
                    R.drawable.coner_button_disable_bg2,
                    R.drawable.coner_button_disable_bg2,
                    displayView
                )
                loading.hideDialog()
                //
                camerax_preview_photo_parent.visibility = View.VISIBLE
            }, 1250)
        }
        camerax_capture_button_1_3_area.visibility = View.VISIBLE
        camerax_capture_button.visibility = View.GONE

        camerax_capture_button_1.setClickEventHandler {
            _filePath = null
            camerax_capture_button_1_3_area.visibility = View.GONE
            camerax_capture_button.visibility = View.VISIBLE
            camerax_preview_photo_parent.visibility = View.GONE
        }
        camerax_capture_button_3.setClickEventHandler {
            _filePath?.also { _fp ->
                val image: Bitmap =
                    (camerax_preview_photo.getDrawable() as BitmapDrawable).getBitmap()
                val bos = BufferedOutputStream(FileOutputStream(_fp));
                image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

                //ToastUtil.showToast(this@CameraXActivity, "$_filePath")
                setResult(
                    RESULT_OK,
                    Intent().also { it.putExtras(Bundle().also { it.putString("filePath", _fp) }) })
                finish()
            }
        }
    }

    //---------------------------------------------------------------------------


    fun returnRotatePhoto(originpath: String): Bitmap? {
        // 取得图片旋转角度
        val angle: Int = readPictureDegree(originpath)
        Log.d(TAG, "angle=${angle}")
        // 把原图压缩后得到Bitmap对象
        return getCompressPhoto(originpath)?.also { bmp ->
            // 修复图片被旋转的角度
            // 保存修复后的图片并返回保存后的图片路径
            return rotaingImageView(angle, bmp)
        }

    }

    //cyan 因为是后置直接返回90度
    fun readPictureDegree(path: String): Int {
        return 90;
//        var degree = 0
//        try {
//            val exifInterface = ExifInterface(path)
//            val orientation: Int = exifInterface.getAttributeInt(
//                ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_NORMAL
//            )
//            when (orientation) {
//                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
//                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
//                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return degree
    }

    fun rotaingImageView(angle: Int, bitmap: Bitmap?): Bitmap? {
        var _angle = angle
        if (bitmap!!.width < bitmap!!.height) {
            _angle = 270
        }
        Log.i("rotaingImageView", "angle=${angle},${bitmap!!.width},${bitmap!!.height}")
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(_angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            if (bitmap != null) {
                val res = ViewPositionAndSizeGetter().calculate(signCenterArea, this, this)

                returnBm =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//                returnBm =
//                    Bitmap.createBitmap(
//                        bitmap,
//                        res.x,
//                        res.y,
//                        res.x + res.w,
//                        res.y + res.h,
//                        matrix,
//                        true
//                    )
            }
        } catch (e: OutOfMemoryError) {
        }
        if (returnBm == null) {
            returnBm = bitmap
        }
        if (bitmap != returnBm) {
            bitmap!!.recycle()
        }
        return returnBm
    }

    fun getCompressPhoto(path: String?): Bitmap? {
        var options: BitmapFactory.Options? = BitmapFactory.Options()
        options!!.inJustDecodeBounds = false
        options.inSampleSize = 2 // 图片的大小设置为原来的2分之一
        val bmp = BitmapFactory.decodeFile(path, options)
        options = null
        return bmp
    }


//    /**
//     * 使用ContentValues存储图片输出信息
//     */
//    fun getContentValues(): ContentValues {
//        // 创建拍照后输出的图片文件名
//        val fileName = "$PHOTO_PREFIX${
//            SimpleDateFormat(
//                FILENAME,
//                Locale.getDefault()
//            ).format(System.currentTimeMillis())
//        }$PHOTO_EXTENSION"
//        return ContentValues().apply {
//            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
//            // 适配Android Q版本
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//            } else {
//                val fileDir = File(
//                    Environment.getExternalStorageDirectory(),
//                    Environment.DIRECTORY_PICTURES
//                ).also { if (!it.exists()) it.mkdir() }
//                val filePath = fileDir.absolutePath + File.separator + fileName
//                put(MediaStore.Images.Media.DATA, filePath)
//            }
//        }
//    }


    companion object {
        private class WithoutLeakHandler(ui: SuperActivity) : Handler() {
            private var ui: WeakReference<SuperActivity> = WeakReference(ui)

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {

                }
            }
        }

        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyyMMddHHmmss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        //private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        // 拍照
//        private const val PHOTO_PREFIX = "IMG_"
//        private const val FILENAME = "yyyyMMdd_HHmmss"
//        private const val PHOTO_EXTENSION = ".jpg"
//        private const val MIME_TYPE = "image/jpg"
//
//        const val TAKE_PICTURE_RESULT = "take_picture_result"

        fun getOutputDirectory(activity: AppCompatActivity): File {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    ?: activity.filesDir
            } else {
                val mediaDir = activity.externalMediaDirs.firstOrNull()?.let {
                    File(it, activity.resources.getString(R.string.app_name)).apply { mkdirs() }
                }
                return if (mediaDir != null && mediaDir.exists())
                    mediaDir else activity.filesDir
            }
        }
    }

    private var mHandler: Handler = WithoutLeakHandler(this)


}