package xsp;

/**
 *
 * @author Solkin
 */
public class ByteString {

    public byte[] byteString = null;

    public ByteString(){
        this.byteString = new byte[]{};
    }

    public ByteString(byte[] byteString){
        this.byteString = byteString;
    }

    public byte[] substring(int startPos, int endPos){
        byte[] subByte = new byte[endPos-startPos];
        System.arraycopy(byteString, startPos, subByte, 0, endPos-startPos);
        return subByte;
    }

    public byte[] append(byte[] toAdd){
        byte[] subByte = new byte[byteString.length + toAdd.length];
        System.arraycopy(byteString, 0, subByte, 0, byteString.length);
        System.arraycopy(toAdd, 0, subByte, byteString.length, toAdd.length);
        byteString = subByte;
        return byteString;
    }

    public int length(){
        return byteString.length;
    }

    public String getString(){
        return new String(byteString);
    }

}