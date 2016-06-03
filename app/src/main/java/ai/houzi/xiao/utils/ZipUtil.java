package ai.houzi.xiao.utils;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 压缩文件用的
 */
public class ZipUtil {
    private int fileIndex = 0;
    private int fileCount = 0;
    private int writeSize = 0;
    private Handler mHandler;

    public ZipUtil() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 压缩文件或文件夹
     *
     * @param srcFilePath 要压缩的文件资源路径
     * @param zipFilePath 压缩后保存资源路径
     * @throws Exception
     */
    public void zipFolder(String srcFilePath, String zipFilePath) {
        new ZipThread(srcFilePath, zipFilePath).start();
//        // 打开要输出的文件
//        java.io.File file = new java.io.File(srcFilePath);
//        if (zipListener != null) {
//            zipListener.startZip(file.getName() + ".zip");
//        }
//        try {
//            findFileCount(file);
//            if (zipListener != null) {
//                zipListener.countFileCount(fileCount);
//            }
//            // 创建Zip包
//            java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new FileOutputStream(
//                    zipFilePath + file.getName() + ".zip"));
//            // 压缩
//            zipFiles(file.getParent() + java.io.File.separator, file.getName(), outZip);
//            if (zipListener != null) {
//                zipListener.progressTotalZip(100);
//            }
//            // 完成,关闭
//            outZip.finish();
//            outZip.close();
//            if (zipListener != null) {
//                zipListener.endZip(true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (zipListener != null) {
//                zipListener.endZip(false);
//            }
//        }
    }

    class ZipThread extends Thread {
        String srcFilePath;
        String zipFilePath;

        public ZipThread(String srcFilePath, String zipFilePath) {
            this.srcFilePath = srcFilePath;
            this.zipFilePath = zipFilePath;
        }

        @Override
        public void run() {
            // 打开要输出的文件
            java.io.File file = new java.io.File(srcFilePath);
            if (zipListener != null) {
                zipListener.startZip(file.getName() + ".zip");
            }
            try {
                findFileCount(file);
                if (zipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zipListener.countFileCount(fileCount);
                        }
                    });
                }
                // 创建Zip包
                java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(new FileOutputStream(
                        zipFilePath + file.getName() + ".zip"));
                // 压缩
                zipFiles(file.getParent() + java.io.File.separator, file.getName(), outZip);
                if (zipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zipListener.progressTotalZip(100);
                        }
                    });
                }
                // 完成,关闭
                outZip.finish();
                outZip.close();
                if (zipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zipListener.endZip(true);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logg.e(e.getMessage());
                if (zipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zipListener.endZip(false);
                        }
                    });
                }
            }
        }
    }

    private void zipFiles(String folderPath, String filePath, java.util.zip.ZipOutputStream zipOut)
            throws Exception {
        if (zipOut == null) {
            return;
        }
        java.io.File file = new java.io.File(folderPath + filePath);
        // 判断是不是文件
        if (file.isFile()) {
            writeSize = 0;
            fileIndex++;
            if (zipListener != null && fileCount > 0) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        zipListener.progressTotalZip(fileIndex * 100 / fileCount);
                    }
                });
            }
            java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath);
            java.io.FileInputStream inputStream = new java.io.FileInputStream(file);
            zipOut.putNextEntry(zipEntry);
            final String fileName = file.getName();
            final int available = inputStream.available();
            int len;
            byte[] buffer = new byte[100000];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
                if (zipListener != null) {
                    writeSize += len;
                    final int i = (int) (writeSize * 1.0 / available * 100);
                    Logg.i(writeSize + "=====" + available + "=====" + i);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            zipListener.progressFileZip(i, fileName);
                        }
                    });
                }
            }
            if (zipListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        zipListener.progressFileZip(100, fileName);
                    }
                });
            }
            inputStream.close();
            zipOut.closeEntry();
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();
            if (fileList != null) {
                // 如果没有子文件, 则添加进去即可
                if (fileList.length <= 0) {
                    java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(filePath + java.io.File.separator);
                    zipOut.putNextEntry(zipEntry);
                    zipOut.closeEntry();
                }
                // 如果有子文件, 遍历子文件
                for (String sFile : fileList) {
                    zipFiles(folderPath, filePath + java.io.File.separator + sFile, zipOut);
                }
            }
        }
    }

    private void findFileCount(File file) {
        if (file.isFile()) {
            fileCount++;
        } else {
            // 文件夹的方式,获取文件夹下的子文件
//            String fileList[] = file.list();
//            if (fileList != null) {
//                // 如果有子文件, 遍历子文件
//                for (String sFile : fileList) {
//                    findFileCount(new File(sFile));
//                }
//            }
            File[] files = file.listFiles();
            if (files != null) {
                // 如果有子文件, 遍历子文件
                for (File sFile : files) {
                    findFileCount(sFile);
                }
            }
        }
    }

    private ZipListener zipListener;

    public interface ZipListener {
        /**
         * @param zipName 压缩文件名
         */
        void startZip(String zipName);

        /**
         * @param isSuccess 是否压缩成功
         */
        void endZip(boolean isSuccess);

        /**
         * @param progress 当前文件进度
         */
        void progressFileZip(int progress, String fileName);

        /**
         * @param progress 当前总进度
         */
        void progressTotalZip(int progress);

        /**
         * @param count 统计压缩文件数量
         */
        void countFileCount(int count);
    }

    public void setZipListener(ZipListener listener) {
        this.zipListener = listener;
    }

    /**
     * 解压文件或文件夹
     *
     * @param zipFile   要解压目标文件
     * @param targetDir 解压到目标文件路径
     * @param isRename  是否重命名(不重命名targetDir不加文件或文件夹的名)
     */
    public void Unzip(String zipFile, String targetDir, boolean isRename) {
        new UnZipThread(zipFile, targetDir, isRename).start();
    }

    class UnZipThread extends Thread {
        String zipFile;
        String targetDir;
        boolean isRename;
        int unZipSize = 0;

        public UnZipThread(String zipFile, String targetDir, boolean isRename) {
            this.zipFile = zipFile;
            this.targetDir = targetDir;
            this.isRename = isRename;
        }

        @Override
        public void run() {
            final File file = new File(zipFile);
            if (unZipListener != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        unZipListener.startUnZip(file.getName());
                    }
                });
            }
            int BUFFER = 4096; //这里缓冲区我们使用4KB，
            String strEntry; //保存每个zip的条目名称

            BufferedOutputStream dest = null; //缓冲输出流
            try {
                FileInputStream fis = new FileInputStream(zipFile);
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
                ZipEntry entry; //每个zip条目的实例
                final int available = fis.available();
                while ((entry = zis.getNextEntry()) != null) {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(isRename ? targetDir : entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }


                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        unZipSize += count;
                        dest.write(data, 0, count);

                        if (unZipListener != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    unZipListener.progressUnZip((int) (unZipSize * 1.0 / available * 100));
                                }
                            });
                        }
                    }
                    dest.flush();
                    dest.close();
                }
                zis.close();
                if (unZipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            unZipListener.endUnZip(true);
                        }
                    });
                }
            } catch (Exception e) {
                if (unZipListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            unZipListener.endUnZip(false);
                        }
                    });
                }
            }
        }
    }

    private UnZipListener unZipListener;

    public interface UnZipListener {
        /**
         * @param unZipName 解压缩文件名
         */
        void startUnZip(String unZipName);

        /**
         * @param isSuccess 是否解压缩成功
         */
        void endUnZip(boolean isSuccess);

        /**
         * @param progress 当前总进度
         */
        void progressUnZip(int progress);
    }

    public void setUnZipListener(UnZipListener listener) {
        this.unZipListener = listener;
    }
}