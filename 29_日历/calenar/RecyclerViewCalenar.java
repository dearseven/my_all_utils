
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.m2u.ifengbigdata.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 自定义的日历控件
 * Created by wx on 2016/11/21.
 */

public class RecyclerViewCalenar extends RecyclerView.Adapter<RecyclerViewCalenar.CalendarItem> implements View.OnClickListener {
    /**
     * 当前年
     */
    private int displayYear;
    /**
     * 当前月
     */
    private int displayMonth;

    /**
     * 上一月的按钮的id
     */
    private int lastId;
    /**
     * 下一月的按钮的id
     */
    private int nextId;

    /**
     * 日期的列表，用于显示
     */
    private List<Day> days = new ArrayList<>();
    /**
     * 这个是接口的对象，要调用这个日历控件的类需要实现这个接口
     */
    private Parent parent;
    /**
     * 显示日历的控件
     */
    private RecyclerView rv;
    /**
     * 上一月
     */
    private TextView last;
    /**
     * 下一月
     */
    private TextView next;
    /**
     * 显示年月
     */
    private TextView show;

    /**
     * 构造方法
     *
     * @param parent         实现parent接口的对象
     * @param recyclerViewId 用于日历的recyclerview的id
     * @param lastId         上一月的点击按钮id （textview）
     * @param nextId         下一月的点击按钮id （textview）
     * @param showId         用于显示当前年月的控件的id（textview）
     */
    public RecyclerViewCalenar(Parent parent, int recyclerViewId, int lastId, int nextId, int showId) {
        this.lastId = lastId;
        this.nextId = nextId;

        Calendar c = Calendar.getInstance();
        //Log.i("today day of week:", c.get(Calendar.DAY_OF_WEEK) + "");

        this.parent = parent;

        this.last = (TextView) parent.findAndroidViewById(lastId);
        this.next = (TextView) parent.findAndroidViewById(nextId);
        this.show = (TextView) parent.findAndroidViewById(showId);

        this.last.setOnClickListener(this);
        this.next.setOnClickListener(this);

        mkCalendarData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));

        this.rv = (RecyclerView) parent.findAndroidViewById(recyclerViewId);
        this.rv.setLayoutManager(new GridLayoutManager(parent.getContext(), 7));
        this.rv.addItemDecoration(new DividerGridItemDecoration(parent.getContext()));
        this.rv.setItemAnimator(new DefaultItemAnimator());
        this.rv.setAdapter(this);
        this.notifyDataSetChanged();
    }


    /**
     * RecyclerView.Adapter
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CalendarItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item_view, parent, false);
        CalendarItem ci = new CalendarItem(itemView);
        return ci;
    }

    /**
     * RecyclerView.Adapter
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(CalendarItem holder, int position) {
        Day day = days.get(position);

        if (day.y == 0) {
            holder.day.setVisibility(View.GONE);
            holder.des.setVisibility(View.VISIBLE);
        } else if (day.y == -1) {
            holder.day.setVisibility(View.GONE);
            holder.des.setVisibility(View.GONE);
        } else {
            holder.day.setVisibility(View.VISIBLE);
            holder.des.setVisibility(View.VISIBLE);
        }
        holder.day.setText(day.day + "");
        holder.des.setText(day.des);

        //if (day.isDay) {
        //  holder.bgi.setBackgroundResource(R.drawable.corner_circle);
        //} else {
        holder.bgi.setBackgroundColor(Color.parseColor(day.bgiColor));
        //}

        //holder.allBg.setBackgroundColor(Color.parseColor(day.bgiColor));
        holder.day.setTextColor(Color.parseColor(day.dayColor));
        holder.des.setTextColor(Color.parseColor(day.desColor));

        int itemW = parent.getContext().getResources().getDisplayMetrics().widthPixels / 9;
        int itemH = itemW;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.bgi.getLayoutParams();
        lp.width = itemW;
        lp.height = itemH;

        holder.allBg.setOnClickListener(new DateClick(position));
    }

    /**
     * RecyclerView.Adapter
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == this.nextId) {
            nextMonth();
        } else if (view.getId() == this.lastId) {
            lastMonth();
        }
    }


    /**
     * 这个click是日历某一天的click
     */
    private class DateClick implements View.OnClickListener {
        private int index;

        public DateClick(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            Day d = days.get(index);
            parent.clickDate(d.y, d.m, d.day);
        }
    }

    /**
     * viewholder
     * 3个控件
     */
    public static class CalendarItem extends RecyclerView.ViewHolder {
        public TextView day;
        public TextView des;
        public ImageView bgi;
        public RelativeLayout allBg;

        public CalendarItem(View itemView) {
            super(itemView);
            allBg = (RelativeLayout) itemView.findViewById(R.id.calendar_item_whole_bg);
            bgi = (ImageView) itemView.findViewById(R.id.calendar_item_bgi);
            day = (TextView) itemView.findViewById(R.id.calendar_item_day);
            des = (TextView) itemView.findViewById(R.id.calendar_item_des);
        }
    }

    /**
     * 使用者记得实现这个接口
     */
    public interface Parent<T> {
        /**
         * 使用者传回自己的context，就是activity
         *
         * @return
         */
        Context getContext();

        /**
         * 使用者在这里写自己的findViewById(int id)
         *
         * @param id
         * @return
         */
        View findAndroidViewById(int id);

        /**
         * 这个方法是日历控件回调，传回年月日给使用者
         *
         * @param y
         * @param m
         * @param d
         */
        void clickDate(int y, int m, int d);
    }

    /**
     * 日历头顶显示当前年月
     */
    private void showYandM() {
        this.show.setText(getDispalyYear() + " - " + (getDisplayMonth() + 1));
    }
    //-----------------给activity/fragment的操作方法-------------------

    /**
     * 获取当前年
     */
    public int getDispalyYear() {
        return displayYear;
    }

    /**
     * 获取当前月
     */
    public int getDisplayMonth() {
        return displayMonth;
    }


    /**
     * 下个月
     */
    public void nextMonth() {
        if (this.displayMonth + 1 > 11) {
            this.displayMonth = 0;
            this.displayYear++;
        } else {
            this.displayMonth++;
        }

        mkCalendarData(this.displayYear, this.displayMonth);
        notifyDataSetChanged();
    }

    /**
     * 上个月
     */
    public void lastMonth() {
        if (this.displayMonth - 1 < 0) {
            this.displayMonth = 11;
            this.displayYear--;
        } else {
            this.displayMonth--;
        }
        mkCalendarData(this.displayYear, this.displayMonth);
        notifyDataSetChanged();
    }

    /**
     * 修改描述的文本
     * @param year
     * @param month
     * @param day
     * @param des
     */
    public void changeDes(int year, int month, int day, String des) {
        for (int i = 0; i < days.size(); i++) {
            Day d = days.get(i);
            if (d.y == year && d.m == month && d.day == day) {
                d.des = des;
                days.set(i, d);
                break;
            }
        }
    }

    /**
     * 修改日期的颜色
     * @param year
     * @param month
     * @param day
     * @param color
     */
    public void changeDayColor(int year, int month, int day, String color) {
        for (int i = 0; i < days.size(); i++) {
            Day d = days.get(i);
            if (d.y == year && d.m == month && d.day == day) {
                d.dayColor = color;
                days.set(i, d);
                break;
            }
        }
    }

    /**
     * 修改描述的颜色
     * @param year
     * @param month
     * @param day
     * @param color
     */
    public void changeDesColor(int year, int month, int day, String color) {
        for (int i = 0; i < days.size(); i++) {
            Day d = days.get(i);
            if (d.y == year && d.m == month && d.day == day) {
                d.desColor = color;
                days.set(i, d);
                notifyDataSetChanged();
                break;
            }
        }
    }

    /**
     * 修改单元格的背景色
     * @param year
     * @param month
     * @param day
     * @param color
     */
    public void changeBgiColor(int year, int month, int day, String color) {
        for (int i = 0; i < days.size(); i++) {
            Day d = days.get(i);
            if (d.y == year && d.m == month && d.day == day) {
                d.bgiColor = color;
                days.set(i, d);
                break;
            }
        }
    }

    /**
     * 修改了以后，调用这个方法刷新
     */
    public void reshow() {
        notifyDataSetChanged();
    }
//----------------------------------------------------------------------

    /**
     * 生成日历的数据
     *
     * @param y
     * @param m （0～11）
     */
    public void mkCalendarData(int y, int m) {
        displayYear = y;
        displayMonth = m;
        showYandM();
        days.clear();
        for (int i = 0; i < 7; i++) {
            Day d = new Day();
            d.y = 0;
            d.m = 0;
            d.day = 0;
            switch (i) {
                case 0:
                    d.dayOfWeek = 1;
                    d.des = "一";
                    break;
                case 1:
                    d.dayOfWeek = 2;
                    d.des = "二";
                    break;
                case 2:
                    d.dayOfWeek = 3;
                    d.des = "三";
                    break;
                case 3:
                    d.dayOfWeek = 4;
                    d.des = "四";
                    break;
                case 4:
                    d.dayOfWeek = 5;
                    d.des = "五";
                    break;
                case 5:
                    d.dayOfWeek = 6;
                    d.des = "六";
                    break;
                case 6:
                    d.dayOfWeek = 7;
                    d.des = "日";
                    break;
            }
            days.add(d);
        }

        KeyDay kd = new KeyDay();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, y);
		c.set(Calendar.DAY_OF_MONTH,1);
        c.set(Calendar.MONTH, m);

        Calendar todayC = Calendar.getInstance();
        if (todayC.get(Calendar.YEAR) == c.get(Calendar.YEAR) && todayC.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
            kd.today = todayC.get(Calendar.DAY_OF_MONTH);
        }
        //获取这个月的天数
        kd.maxDayCountOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        //获取这个月的第一天的星期几
        c.set(Calendar.DAY_OF_MONTH, 1);
        kd.firstDayInWeekOfMonth = c.get(Calendar.DAY_OF_WEEK);
        if (kd.firstDayInWeekOfMonth == 1) {
            kd.firstDayInWeekOfMonth = 7;//按照我们中国人的习惯来嘛,周日是7
        } else if (kd.firstDayInWeekOfMonth == 2) {
            kd.firstDayInWeekOfMonth = 1;//
        } else if (kd.firstDayInWeekOfMonth == 3) {
            kd.firstDayInWeekOfMonth = 2;//
        } else if (kd.firstDayInWeekOfMonth == 4) {
            kd.firstDayInWeekOfMonth = 3;//
        } else if (kd.firstDayInWeekOfMonth == 5) {
            kd.firstDayInWeekOfMonth = 4;//
        } else if (kd.firstDayInWeekOfMonth == 6) {
            kd.firstDayInWeekOfMonth = 5;//
        } else if (kd.firstDayInWeekOfMonth == 7) {
            kd.firstDayInWeekOfMonth = 6;//
        }

        int loopTimes = kd.firstDayInWeekOfMonth + kd.maxDayCountOfMonth - 1;
        for (int i = 0; i < loopTimes; i++) {
            Day day = new Day();
            if ((i + 1) < kd.firstDayInWeekOfMonth) {
                //Log.i("xdcheckin", "还没开始");
                day.y = -1;
            } else {
                int dayOfMonth = (i + 1) - kd.firstDayInWeekOfMonth + 1;
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 1) {
                    dayOfWeek = 7;
                    day.des = "休";
                } else if (dayOfWeek == 2) {
                    dayOfWeek = 1;
                    day.des = "班";
                } else if (dayOfWeek == 3) {
                    dayOfWeek = 2;
                    day.des = "班";
                } else if (dayOfWeek == 4) {
                    dayOfWeek = 3;
                    day.des = "班";
                } else if (dayOfWeek == 5) {
                    dayOfWeek = 4;
                    day.des = "班";
                } else if (dayOfWeek == 6) {
                    dayOfWeek = 5;
                    day.des = "班";
                } else if (dayOfWeek == 7) {
                    dayOfWeek = 6;
                    day.des = "休";
                }
                day.y = y;
                day.m = m;
                day.day = dayOfMonth;
                day.dayOfWeek = dayOfWeek;
                if (kd.today == day.day) {
                    day.isDay = true;
                }
            }
            days.add(day);
        }
    }

    /**
     * 这个是用于显示的数据
     */
    public static class Day {
        public int y;
        public int m;
        public int day;
        public String des;
        public int dayOfWeek;
        public boolean isDay = false;
        public String dayColor = "#333333";
        public String desColor = "#666666";
        public String bgiColor = "#efefef";
    }

    /**
     * 每个月的一些关键日子，纪录下来放到这里，容易找啊，计算过程用到了，外面不要调用这个
     */
    public class KeyDay {
        /**
         * 这个有多少天
         */
        int maxDayCountOfMonth = 0;
        /**
         * 如果是今天，则这里不会是-1
         */
        int today = -1;
        /**
         * 这个月的第一天是星期几
         */
        int firstDayInWeekOfMonth = 0;
    }

}
