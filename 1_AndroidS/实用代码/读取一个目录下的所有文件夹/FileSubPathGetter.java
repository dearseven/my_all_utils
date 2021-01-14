
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 给予一个目的，迭代出自身和一套子目录的结构，
 */
public class FileSubPathGetter {

    /**
     * 给予一个目的，迭代出自身和一套子目录的结构，
     *
     * @param root
     * @return
     */
    public List<FolderNode> getPathes(String root) {
        Set<String> pathes = pathesGet(root);
        List<FolderNode> nodes = new ArrayList<FolderNode>(pathes.size() + 1);
        Iterator<String> it = pathes.iterator();
        while (it.hasNext()) {
            nodes.add(new FolderNode(it.next()));
        }
        Collections.sort(nodes);
        return nodes;
    }

    private Set<String> pathesGet(String root) {
        Set<String> pathes = new HashSet<String>();
        pathes.add(root/*.replaceAll("\\\\", "/")*/);
        File rootFile = new File(root);
        pathes= recursive(rootFile, pathes);
        return pathes;
    }

    private Set<String> recursive(File f, Set<String> pathes) {
        for (int i = 0; i < f.listFiles().length; i++) {
            if (f.listFiles()[i].isDirectory()) {
                pathes.add(f.listFiles()[i].getAbsolutePath()/*.replaceAll("\\\\", "/")*/);
                recursive(f.listFiles()[i], pathes);
            }
        }
        return pathes;
    }

    public static class FolderNode implements Comparable<FolderNode> {
        @Override
        public String toString() {
            return "FolderNode{" +
                    "path='" + path + '\'' +
                    ", level=" + level +
                    '}';
        }

        public String path;
        public int level;

        public FolderNode(String path) {
            this.path = path;
            if (path.indexOf("\\") > -1) {
                level = path.split("\\\\").length ;
            } else {
                level = path.split("\\/").length;
            }
        }

        @Override
        public int compareTo(FolderNode o) {
            return level - o.level;
        }
    }
}
