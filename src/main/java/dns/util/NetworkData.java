package dns.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class NetworkData {
    private byte[] buf;

    private int index = 0;
    private int markedIndex = 0;

    private int byte1 = 0;
    private int byte2 = 0;
    private int byte3 = 0;
    private int byte4 = 0;

    private ByteArrayOutputStream backing;
    private DataOutput writeBuffer;
    private int writerIndex;

    public NetworkData(int size) {
        this.backing = new ByteArrayOutputStream(size);
        this.writeBuffer = new DataOutputStream(this.backing);
    }

    public NetworkData() {
        this(4096);
    }

    public NetworkData(byte[] data) {
        this.buf = data;
        this.index = 0;
    }

    public long readUnsignedInt() {
        this.byte1 = (0xFF & this.buf[this.index]);
        this.byte2 = (0xFF & this.buf[this.index + 1]);
        this.byte3 = (0xFF & this.buf[this.index + 2]);
        this.byte4 = (0xFF & this.buf[this.index + 3]);

        this.index += 4;

        return ((long) (this.byte1 << 24 | this.byte2 << 16 | this.byte3 << 8 | this.byte4)) & 0xFFFFFFFFL;
    }

    public short readUnsignedByte(){
        this.byte1 = (0xFF & this.buf[this.index]);
        this.index++;
        return (short)this.byte1;
    }

    public char readUnsignedChar(){
        this.byte1 = (0xFF & this.buf[this.index]);
        this.byte2 = (0xFF & this.buf[this.index+1]);
        this.index += 2;
        return (char) (this.byte1 << 8 | this.byte2);
    }

    public void readBytes(byte[] destination){
        System.arraycopy(this.buf,this.index,destination,0,destination.length);
        this.index += destination.length;
    }

    public void writeChar(int c){
        try{
            this.writeBuffer.writeChar(c);
            this.writerIndex+=2;
        }catch(IOException e){
            throw new RuntimeException("Error while writing data",e);
        }
    }


    public void writeByte(int b){
        try{
            this.writeBuffer.write(b);
            this.writerIndex++;
        }catch(IOException e){
            throw new RuntimeException("Error while writig data",e);
        }
    }

    public void writeBytes(byte[] b){
        try {
            this.writeBuffer.write(b);
            this.writerIndex += b.length;
        } catch (IOException e) {
            throw new RuntimeException("Error while writing data", e);
        }
    }

    public void writeInt(long i){
        try {
            this.writeBuffer.writeInt((int)i);
            this.writerIndex+=4;
        } catch (IOException e) {
            throw new RuntimeException("Error while writing data", e);
        }
    }

    public byte[] write(){
        byte[] data = backing.toByteArray();
        return Arrays.copyOf(data, this.writerIndex);
    }

    public int readableBytes(){
        if(this.buf != null){
            return this.buf.length;
        }

        return 0;
    }

    public int writableBytes(){
        return this.writerIndex;
    }

    public int getReaderIndex() {
        return this.index;
    }

    public void setReaderIndex(int index) {
        this.index = index;
    }

    public int getWriterIndex() {
        return this.writerIndex;
    }

    public void markReaderIndex(){
        this.markedIndex = index;
    }

    public void resetReaderIndex(){
        this.index = markedIndex;
    }
}
