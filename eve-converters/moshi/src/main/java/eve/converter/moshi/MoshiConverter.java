package eve.converter.moshi;

import com.squareup.moshi.Moshi;

import java.io.IOException;

import is.uncommon.eve.Converter;
import okio.Buffer;

/**
 * An object instance representing MoshiConverter.
 *
 * Serialize an object to bytes and vice versa.
 */
public class MoshiConverter implements Converter {

    Moshi moshi;

    MoshiConverter(Moshi moshi) {
        this.moshi = moshi;
    }

    /**
     * @param moshi - {@link Moshi}
     * @return {@link MoshiConverter}
     */
    public static MoshiConverter create(Moshi moshi) {
        return new MoshiConverter(moshi);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] data, Class<T> type) {
        if (moshi == null || data == null || type == null) {
            return null;
        }

        Buffer buffer = new Buffer();
        buffer.write(data);

        try {
            return (T) moshi.adapter(type).fromJson(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public byte[] serialize(Object object, Class type) {
        Buffer buffer = new Buffer();
        try {
            moshi.adapter(type).toJson(buffer, object);
            return buffer.readByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
