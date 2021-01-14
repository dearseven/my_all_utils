
import android.content.Context
import android.content.pm.PackageInfo
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class SeveralUtils {
    constructor()

    fun isAppInstalled(context: Context, pkgName: String) =
        checkAppInstalled1(context, pkgName) || checkAppInstalled2(context, pkgName)


    //-------------------------------------------------------------------------------------
    //    fun isInstallByread(string): Boolean {
//        return File("/data/data/${packageName}").exists();
//    }
    private fun checkAppInstalled1(context: Context, pkgName: String): Boolean {
        if (pkgName == null || pkgName.isEmpty()) {
            return false
        }
        var packageInfo: PackageInfo? = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (e: Exception) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null
    }

    private fun checkAppInstalled2(context: Context, pkgName: String): Boolean {
        if (pkgName == null || pkgName.isEmpty()) {
            return false
        }
        val packageManager = context.getPackageManager();
        val info = packageManager.getInstalledPackages(0);
        if (info == null || info.isEmpty())
            return false;
        for (i in 0 until info.size) {
            if (pkgName.equals(info.get(i).packageName)) {
                return true;
            }
        }
        return false;
    }
}