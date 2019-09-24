public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        final TextView tv_num = (TextView) findViewById(R.id.tv_num);
 
 
        Fragment fragment1 = PhotoFragment.newInstance("http://img1.3lian.com/2015/w22/87/d/105.jpg");
        Fragment fragment2 = PhotoFragment.newInstance("http://a3.topitme.com/0/d0/f1/1128126520d81f1d00o.jpg");
        Fragment fragment3 = PhotoFragment.newInstance("http://pic30.nipic.com/20130605/7447430_151725918000_2.jpg");
        Fragment fragment4 = PhotoFragment.newInstance("http://img1.3lian.com/2015/a1/46/d/100.jpg");
        Fragment fragment5 = PhotoFragment.newInstance("http://img2.imgtn.bdimg.com/it/u=294543409,575604841&fm=26&gp=0.jpg");
        Fragment fragment6 = PhotoFragment.newInstance("http://pic28.photophoto.cn/20130809/0036036894761435_b.jpg");
 
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);
        fragmentList.add(fragment6);
 
        tv_num.setText(1+"/"+fragmentList.size());
        PhotoFragmentAdapter adapter = new PhotoFragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
 
            }
 
            @Override
            public void onPageSelected(int position) {
              tv_num.setText(position+1+"/"+fragmentList.size());
            }
 
            @Override
            public void onPageScrollStateChanged(int state) {
 
            }
        });
        viewpager.setCurrentItem(2);
    }
}
