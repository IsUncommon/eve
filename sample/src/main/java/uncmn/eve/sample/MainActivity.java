package uncmn.eve.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.ryanharter.auto.value.moshi.AutoValueMoshiAdapterFactory;
import com.squareup.moshi.Moshi;
import uncmn.eve.Eve;
import uncmn.eve.Store;
import uncmn.eve.converter.moshi.MoshiConverter;
import uncmn.eve.store.sql.SqlStore;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  Eve eve;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Moshi moshi = new Moshi.Builder().add(new AutoValueMoshiAdapterFactory()).build();
    MoshiConverter converter = MoshiConverter.create(moshi);
    converter.map(SampleObjectOne.CONVERTER_KEY, SampleObjectOne.class);
    converter.map(SampleObjectTwo.CONVERTER_KEY, SampleObjectTwo.class);
    SqlStore sqlStore = SqlStore.create(this, converter);

    eve = Eve.builder(this).store(sqlStore).build();
    Store store = eve.store();

    ////Add Primitives
    String intKey = "intKey";
    int val = 1;
    store.set(intKey, val);
    boolean same = ((int) store.get(intKey) == val);
    Log.d(TAG, "Are ints same -- " + same);

    String floatKey = "floatKey";
    float floatVal = 41f;
    store.set(floatKey, floatVal);
    same = ((float) store.get(floatKey) == floatVal);
    Log.d(TAG, "Are floats same -- " + same);

    String doubleKey = "doubleKey";
    double doubleVal = 42.0;
    store.set(doubleKey, doubleVal);
    same = ((double) store.get(doubleKey) == doubleVal);
    Log.d(TAG, "Are doubles same -- " + same);

    String longKey = "longKey";
    long longVal = 44L;
    store.set(longKey, longVal);
    same = ((long) store.get(longKey) == longVal);
    Log.d(TAG, "Are longs same -- " + same);

    String booleanKey = "booleanKey";
    boolean booleanValue = false;
    store.set(booleanKey, booleanValue);
    same = ((boolean) store.get(booleanKey) == booleanValue);
    Log.d(TAG, "Are booleans same -- " + same);

    String charKey = "charKey";
    char charValue = 'Y';
    store.set(charKey, charValue);
    same = ((char) store.get(charKey) == charValue);
    Log.d(TAG, "Are chars same -- " + same);

    String byteKey = "byteKey";
    byte byteValue = (byte) 'N';
    store.set(byteKey, byteValue);
    same = ((byte) store.get(byteKey) == byteValue);
    Log.d(TAG, "Are bytes same -- " + same);

    String stringKey = "stringKey";
    String stringValue = "Hello World!";
    store.set(stringKey, stringValue);
    Log.d(TAG, "Are strings same -- " + store.get(stringKey));

    ////End Primitives

    ////Add Objects
    String complexKey1 = "complex1";
    SampleObjectOne obj1 = SampleObjectOne.create(complexKey1, new float[] { 1f, 2.5f });
    store.set(complexKey1, obj1);

    String complexKey2 = "complex2";
    SampleObjectOne obj2 = SampleObjectOne.create(complexKey2, new float[] { 2f, 2.5f });
    store.set(complexKey2, obj2);

    String complexKey3 = "complex3";
    SampleObjectOne obj3 = SampleObjectOne.create(complexKey3, new float[] { 3f, 3.5f });
    store.set(complexKey3, obj3);

    SampleObjectOne retObj3 = eve.store().get(complexKey3);
    Log.d(TAG, "Is obj3 same ? " + obj3.equals(retObj3));

    String sampleKey1 = "sample1";
    SampleObjectTwo sample1 = SampleObjectTwo.create(sampleKey1, new int[] { 1, 2 }, obj1);
    store.set(sampleKey1, sample1);

    String sampleKey2 = "sample2";
    SampleObjectTwo sample2 = SampleObjectTwo.create(sampleKey2, new int[] { 2, 3 }, obj2);
    store.set(sampleKey2, sample2);

    String sampleKey3 = "sample3";
    SampleObjectTwo sample3 = SampleObjectTwo.create(sampleKey3, new int[] { 3, 4 }, obj3);
    store.set(sampleKey3, sample3);

    SampleObjectTwo retSample3 = eve.store().get(sampleKey3);
    Log.d(TAG, "Is sample same ? " + sample3.equals(retSample3));
    Log.d(TAG, "Sample object is -- " + retSample3);

    ////End Objects
  }
}
