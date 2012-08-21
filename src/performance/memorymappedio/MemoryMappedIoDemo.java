package performance.memorymappedio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public final class MemoryMappedIoDemo {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        File tempFile = File.createTempFile("memmappediotest", "");
        tempFile.deleteOnExit();

        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        
        Record record = new Record();
        record.setUpdateTime(0l);
        record.setSex('M');
        record.setAge(20);
        record.setName("Foo Bar");

        write(raf, record);
        raf.getFD().sync();

        int iterations = 10;

        for (int i = 1; i <=5; i++) {
            long start = System.currentTimeMillis();

            for (int j = 1; j <= iterations; j++) {
                raf.seek(0);
                read(raf, record);

                System.out.println("Read record: " + record);

                record.setUpdateTime(record.getUpdateTime() + 1);
                raf.seek(0);

                System.out.println("Setting timestamp: " + record.getUpdateTime());
                write(raf, record);
                raf.getFD().sync();
            }

            long diff = System.currentTimeMillis() - start;
            System.out.println("Random Access File took " + diff + "ms.");
        }

        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, 64);

        for (int i = 1; i <=5; i++) {
            long start = System.currentTimeMillis();

            for (int j = 1; j <= iterations; j++) {
                read(buffer, record);

                System.out.println("Read record: " + record);

                record.setUpdateTime(record.getUpdateTime() + 1);
                buffer.rewind();

                System.out.println("Setting timestamp: " + record.getUpdateTime());
                write(buffer, record);
                buffer.force();
            }

            long diff = System.currentTimeMillis() - start;
            System.out.println("Memory Mapped File took " + diff + "ms.");
        }
    }

    private static void write(RandomAccessFile file, Record record) throws IOException {
        file.writeLong(record.getUpdateTime());
        file.writeChar(record.getSex());
        file.writeInt(record.getAge());
        file.write(record.getName().getBytes());
    }

    private static void read(RandomAccessFile file, Record record) throws IOException {
        record.setUpdateTime(file.readLong());
        record.setSex(file.readChar());
        record.setAge(file.readInt());
        byte buff [] = new byte[7];
        file.readFully(buff);
        record.setName(new String(buff));
    }

    private static void write(MappedByteBuffer buffer, Record record) throws IOException {
        buffer.putLong(record.getUpdateTime());
        buffer.putChar(record.getSex());
        buffer.putInt(record.getAge());
        buffer.put(record.getName().getBytes());
    }

    private static void read(MappedByteBuffer buffer, Record record) throws IOException {
        record.setUpdateTime(buffer.getLong());
        record.setSex(buffer.getChar());
        record.setAge(buffer.getInt());
        byte buff [] = new byte[7];
        buffer.get(buff);
        record.setName(new String(buff));
    }

    private static final class Record {
        private long updateTime;
        private char sex;
        private int age;
        private String name;

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public char getSex() {
            return sex;
        }

        public void setSex(char sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Record [updateTime=" + updateTime + ", sex=" + sex + ", age=" + age + ", name=" + name + "]";
        }
    }
}
