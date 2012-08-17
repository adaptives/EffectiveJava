package performance.memorymappedio;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public final class MemoryMappedIoDemo {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(File.createTempFile("memmappediotest", ""), "rw");
        for (int i = 1; i <= 1000000; i++) {
            raf.write(0);
        }

        raf.getFD().sync();

        FileChannel channel = raf.getChannel();
        MappedByteBuffer buff = channel.map(MapMode.READ_WRITE, 0, 1000000l);
        buff.put("Kalpak Ravindra Gadre.".getBytes());
        buff.force();
        System.out.println("Done!");
    }
}

