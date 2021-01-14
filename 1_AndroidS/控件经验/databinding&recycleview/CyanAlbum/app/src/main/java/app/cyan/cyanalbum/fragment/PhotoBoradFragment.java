package app.cyan.cyanalbum.fragment;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.cyan.cyanalbum.BR;
import app.cyan.cyanalbum.R;
import app.cyan.cyanalbum.beans.FileDes;
import app.cyan.cyanalbum.databinding.FragmentPhotoBoradBinding;
import app.cyan.cyanalbum.databinding.ItemviewPhotoBoard1Binding;
import app.cyan.cyanalbum.databinding.ItemviewPhotoBoard2Binding;
import app.cyan.cyanalbum.utils.BitmapHelper;
import app.cyan.cyanalbum.utils.DLog;
import app.cyan.cyanalbum.utils.FileCatcher;
import app.cyan.cyanalbum.utils.LRUImageCache;
import app.cyan.cyanalbum.views.dividers.DividerGridItemDecoration;

public class PhotoBoradFragment extends Fragment {
    private FileCatcher fileCatcher = new FileCatcher();
    public static String TAG = PhotoBoradFragment.class.getSimpleName();
    private FragmentPhotoBoradBinding binding = null;
    private List<BoardItem> data = new ArrayList<BoardItem>();
    private Adapter adapter = null;
    private LRUImageCache cache = new LRUImageCache();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        data.add(BoardItem.get0Item());//默认生成按钮
        // Fragment也是先获取binding
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_photo_borad, container, false);
        initRecycleView();
        new FileSearcher().execute(true, false, false, false);
        //返回getRoot()
        return binding.getRoot();
    }

    /**
     * RecyclerView是用databinding主要是adapter不一样
     * adapter.notifyDataSetChanged()在FileSearcher这个异步任中~
     */
    private void initRecycleView() {
        adapter = new Adapter();
        RecyclerView mRecyclerView = binding.photoBoardRv;
        //设置布局管理器
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        mRecyclerView.setAdapter(adapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(
        //   getActivity(), DividerItemDecoration.VERTICAL));
        //设置item之间的间隔
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(getContext());
        mRecyclerView.addItemDecoration(decoration);
    }

    class Adapter extends RecyclerView.Adapter<ViewKeeper> {
        @Override
        public ViewKeeper onCreateViewHolder(ViewGroup parent, int viewType) {
            //create的时候通过不同的类型获取不同的布局文件，然后ViewHolder（这里是viewkeep）多了一个属性就是设定binding
            // 这里也不是直接获取inflate布局，而是获取binding，视图也是binding.getRoot();
            if (viewType == BoardItem.BoardItemType.TAKE_NEW_PHOTO.ordinal()) {
                ItemviewPhotoBoard2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itemview_photo_board2, parent, false);
                ViewKeeper vk = new ViewKeeper(binding.getRoot());
                vk.setBinding(binding);
                return vk;
            } else {
                ItemviewPhotoBoard1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.itemview_photo_board1, parent, false);
                ViewKeeper vk = new ViewKeeper(binding.getRoot());
                vk.setBinding(binding);
                return vk;
            }
        }

        @Override
        public void onBindViewHolder(ViewKeeper holder, int position) {
            //获取数据
            BoardItem item = data.get(position);
            //获取binding
            ViewDataBinding viewDataBinding = holder.getBinding();

            if (item.getType() == BoardItem.BoardItemType.TAKE_NEW_PHOTO.ordinal()) {//如果是拍照按钮
                ItemviewPhotoBoard2Binding binding = (ItemviewPhotoBoard2Binding) viewDataBinding;
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.itemviewPhotoBoardBtn.getLayoutParams();
                lp.height = getContext().getResources().getDisplayMetrics().widthPixels / 3;
                binding.itemviewPhotoBoardBtn.setLayoutParams(lp);
            } else {//显示图片
                //1通过binding获取试图，然后调整大小
                ItemviewPhotoBoard1Binding binding = (ItemviewPhotoBoard1Binding) viewDataBinding;
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.itemviewPhotoBoardImage.getLayoutParams();
                lp.height = getContext().getResources().getDisplayMetrics().widthPixels / 3;
                binding.itemviewPhotoBoardImage.setLayoutParams(lp);

                //获取信息，主要是拿到bitmap
                FileDes fd = item.getFd();
                //这里是从缓存或者直接拿系统的thumb的bitmap
                if (fd.fileType == FileDes.FILE_TYPE.sys_img || fd.fileType == FileDes.FILE_TYPE.qq_img
                        || fd.fileType == FileDes.FILE_TYPE.wx_img) {
                    if (cache.getBitmapFromMemCache(fd.filePath) == null) {
                        cache.addBitmapToMemoryCache(fd.filePath, BitmapHelper.loadBitmap(fd.filePath));
                    }
                    //*forShowBitmap这个属性做了binding，因为要给imageview
                    item.forShowBitmap = cache.getBitmapFromMemCache(fd.filePath);
                } else if (fd.fileType == FileDes.FILE_TYPE.sys_video) {
                    //*forShowBitmap这个属性做了binding，因为要给imageview
                    item.forShowBitmap = fd.thumb;
                }
                //*Item是ItemviewPhotoBoard1Binding里的data,这个data主要是在ImageView上读了forShowBitmap这个属性
                binding.setItem(item);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).getType();
        }
    }

    public class ViewKeeper extends RecyclerView.ViewHolder {

        public ViewKeeper(View itemView) {
            super(itemView);
        }

        /**
         * ViewHolder直接保存binding方便使用
         */
        private ViewDataBinding binding;

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        //---------
    }

    /**
     * 这个类是数据类啊在layout里作为data引入了。
     * forShowBitmap属性做了Bindable
     */
    public static class BoardItem extends BaseObservable {
        public static BoardItem get0Item() {
            BoardItem item = new BoardItem();
            item.setType(BoardItemType.TAKE_NEW_PHOTO.ordinal());
            return item;
        }

        /**
         * 0 打开相机的按钮
         * 1 普通图片
         * 2 动图
         * 3 视频？
         */
        private int type;
        private String path;
        private FileDes fd;

        /**
         * for binding
         */
        private Bitmap forShowBitmap;

        public void setForShowBitmap(Bitmap forShowBitmap) {
            this.forShowBitmap = forShowBitmap;
            notifyPropertyChanged(BR.forShowBitmap);
        }

        @Bindable
        public Bitmap getForShowBitmap() {
            return forShowBitmap;
        }

        public void setFd(FileDes fd) {
            this.fd = fd;
        }

        public FileDes getFd() {
            return fd;
        }

        enum BoardItemType {
            TAKE_NEW_PHOTO, COMMON_PHOTO, GIF_PHOTO, VIDEO
        }

        public String getPath() {
            return path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
    //------------

    /**
     * 查找文件的异步任务
     */
    private class FileSearcher extends AsyncTask<Boolean, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //do nothing
        }


        @Override
        protected Boolean doInBackground(Boolean... bls) {
            fileCatcher.getAllFiles(getContext(), bls[0], bls[1], bls[2], bls[3]);
            //其实我就是无聊，好久没用个这个东西了
            publishProgress(fileCatcher.files.size());
            return fileCatcher.files.size() > 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int count = values[0];
            //Toast.makeText(MainActivity.this, "找到" + count + "个文件", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            initPullDisplay(aBoolean);
        }
    }


    /**
     * 查询好所有文件，然后显示
     */
    private void initPullDisplay(boolean suc) {
        if (suc == false)
            return;
        //Toast.makeText(getContext(), "找到" + fileCatcher.files.size() + "个文件,即将显示!", Toast.LENGTH_SHORT).show();
        DLog.i(getClass(), "找到的原本文件个数，" + fileCatcher.files.size());
        Collections.reverse(fileCatcher.files);
        for (FileDes fd : fileCatcher.files) {
            boolean needAdd = false;
            BoardItem item = new BoardItem();
            item.setFd(fd);
            if (fd.fileType.ordinal() == FileDes.FILE_TYPE.default_type.ordinal() || fd.fileType.ordinal() == FileDes.FILE_TYPE.sys_img.ordinal() || fd.fileType.ordinal() == FileDes.FILE_TYPE.wx_img.ordinal()) {
                if (fd.fileName.endsWith("gif") || fd.fileName.endsWith("GIF")) {
                    needAdd = false;
                    item.setType(BoardItem.BoardItemType.GIF_PHOTO.ordinal());
                } else {
                    needAdd = true;
                    item.setType(BoardItem.BoardItemType.COMMON_PHOTO.ordinal());
                }
                item.setPath(fd.filePath);
            } else if (fd.fileType.ordinal() == FileDes.FILE_TYPE.sys_video.ordinal()) {
                item.setType(BoardItem.BoardItemType.VIDEO.ordinal());
                item.setPath(fd.thumbPath);
                needAdd = false;
            }
            if (needAdd) {
                data.add(item);
            }
        }
        DLog.i(getClass(), " 过滤后的文件个数：" + (data.size() - 1));

       adapter.notifyDataSetChanged();
    }

}
