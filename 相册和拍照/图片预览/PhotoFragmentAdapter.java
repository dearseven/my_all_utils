public class PhotoFragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;
 
    public PhotoFragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }
 
 
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
 
    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
