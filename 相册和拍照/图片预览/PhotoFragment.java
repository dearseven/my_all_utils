public class PhotoFragment extends Fragment {
    private String url;
    private PhotoView mPhotoView;
 
    /**
     * 获取这个fragment需要展示图片的url
     * @param url
     * @return
     */
    public static PhotoFragment newInstance(String url) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }
 
 
 
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_img, container, false);
        mPhotoView = view.findViewById(R.id.photoview);
        //设置缩放类型，默认ScaleType.CENTER（可以不设置）
        mPhotoView.setScaleType(ImageView.ScaleType.CENTER);
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
 
                //Toast.makeText(getActivity(),"长按事件"+url,Toast.LENGTH_SHORT).show();
 
                mPhotoView.setDrawingCacheEnabled(true);
                Bitmap obmp = Bitmap.createBitmap(mPhotoView.getDrawingCache());
                mPhotoView.setDrawingCacheEnabled(false);
                saveMyBitmap(getActivity(), obmp);
                return true;
            }
        });
        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                Toast.makeText(getActivity(),"点击事件，真实项目中可关闭activity",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        Glide.with(getContext())
                .load(url)
                .placeholder(R.mipmap.ic_launcher)//加载过程中图片未显示时显示的本地图片
                .error(R.mipmap.ic_launcher)//加载异常时显示的图片
//                .centerCrop()//图片图填充ImageView设置的大小
                .fitCenter()//缩放图像测量出来等于或小于ImageView的边界范围,该图像将会完全显示
                .into(mPhotoView);
        return view;
    }
 
 
    //保存文件到指定路径
    public void saveMyBitmap(Context context, Bitmap bitmap) {
        String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
        File appDir = new File(sdCardDir, "HappyBirthday");
        if (!appDir.exists()) {//不存在
            appDir.mkdir();
        }
        String fileName = "HappyBirthday" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        getActivity().sendBroadcast(intent);
        Toast.makeText(getActivity(),"图片保存成功",Toast.LENGTH_SHORT).show();
    }
 
}
