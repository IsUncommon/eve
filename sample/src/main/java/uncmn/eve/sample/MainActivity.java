package uncmn.eve.sample;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.ryanharter.auto.value.moshi.AutoValueMoshiAdapterFactory;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import okio.BufferedSource;
import okio.Okio;
import uncmn.eve.Entry;
import uncmn.eve.Eve;
import uncmn.eve.Store;
import uncmn.eve.converter.moshi.MoshiConverter;
import uncmn.eve.sample.gist.Gist;
import uncmn.eve.store.sql.SqlStore;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  Moshi moshi = new Moshi.Builder().add(new AutoValueMoshiAdapterFactory()).build();
  Eve eve;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MoshiConverter converter = MoshiConverter.create(moshi);
    //Unique converter keys should be mapped and never changed since it is persisted.
    converter.map(Gist.CONVERTER_KEY, Gist.class);
    converter.map(SampleObjectOne.CONVERTER_KEY, SampleObjectOne.class);
    converter.map(SampleObjectTwo.CONVERTER_KEY, SampleObjectTwo.class);
    SqlStore sqlStore = SqlStore.create(this, converter);

    //set variables
    setDbName(R.string.db_name, sqlStore.dbName());
    setDbSize(R.string.db_size, new File(sqlStore.dbPath()).length() / 1000 + " kb");
    setCount(R.string.db_count, sqlStore.count() + "");

    eve = Eve.builder().store(sqlStore).build();
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
    same = store.get(stringKey).equals(stringValue);
    Log.d(TAG, "Are strings same -- " + same);

    String intArrayKey = "intArrayKey";
    int[] intArrayValue = new int[] { 2, 3, 4 };
    store.set(intArrayKey, intArrayValue);
    same = Arrays.toString((int[]) store.get(intArrayKey)).equals(Arrays.toString(intArrayValue));
    Log.d(TAG, "Are int arrays same -- " + same);

    String floatArrayKey = "floatArrayKey";
    float[] floatArrayValue = new float[] { 3f, 4f, 5f };
    store.set(floatArrayKey, floatArrayValue);
    same = Arrays.toString((float[]) store.get(floatArrayKey))
        .equals(Arrays.toString(floatArrayValue));
    Log.d(TAG, "Are float arrays same -- " + same);

    String doubleArrayKey = "doubleArrayKey";
    double[] doubleArrayValue = new double[] { 3.2, 4.3, 5.4 };
    store.set(doubleArrayKey, doubleArrayValue);
    same = Arrays.toString((double[]) store.get(doubleArrayKey))
        .equals(Arrays.toString(doubleArrayValue));
    Log.d(TAG, "Are double arrays same -- " + same);

    String longArrayKey = "longArrayKey";
    long[] longArrayValue = new long[] { 32L, 43L, 54L };
    store.set(longArrayKey, longArrayValue);
    same =
        Arrays.toString((long[]) store.get(longArrayKey)).equals(Arrays.toString(longArrayValue));
    Log.d(TAG, "Are long arrays same -- " + same);

    String charArrayKey = "charArrayKey";
    char[] charArrayValue = new char[] { '2', 'A', 'B' };
    store.set(charArrayKey, charArrayValue);
    same =
        Arrays.toString((char[]) store.get(charArrayKey)).equals(Arrays.toString(charArrayValue));
    Log.d(TAG, "Are char arrays same -- " + same);

    String stringArrayKey = "stringArrayKey";
    String[] stringArrayValue = new String[] { "Integer", "Long", "Boolean" };
    store.set(stringArrayKey, stringArrayValue);
    same = Arrays.toString((String[]) store.get(stringArrayKey))
        .equals(Arrays.toString(stringArrayValue));
    Log.d(TAG, "Are string arrays same -- " + same);

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

    //get key type
    Log.d(TAG, "onCreate: Key type: " + eve.store().type(sampleKey3).getSimpleName());

    ////End Objects

    ////Start collections.
    ArrayList<String> stringListValue = new ArrayList<>();
    stringListValue.add("String1");
    stringListValue.add("String2");
    stringListValue.add("String3");
    String stringListKey = "stringListKey";
    store.set(stringListKey, stringListValue);

    List<String> retStringListValue = eve.store().get(stringListKey);
    same = stringListValue.containsAll(retStringListValue) && retStringListValue.containsAll(
        stringListValue);
    Log.d(TAG, "Are string list same -- " + same);
    Log.d(TAG, "Retrieved string list is -- " + retStringListValue);

    ArrayList<SampleObjectTwo> sampleObjectTwoArrayList = new ArrayList<>();
    sampleObjectTwoArrayList.add(sample1);
    sampleObjectTwoArrayList.add(sample2);
    sampleObjectTwoArrayList.add(sample3);
    String objectListKey = "objectListKey";
    store.set(objectListKey, sampleObjectTwoArrayList);

    List<SampleObjectTwo> retObjectListValue = eve.store().get(objectListKey);
    same =
        sampleObjectTwoArrayList.containsAll(retObjectListValue) && retObjectListValue.containsAll(
            sampleObjectTwoArrayList);
    Log.d(TAG, "Are string list same -- " + same);
    Log.d(TAG, "Retrieved object list is -- " + sampleObjectTwoArrayList);

    ////End collections.

    ////start query
    List<Entry<SampleObjectOne>> result =
        store.query().keyContains("plex").type(SampleObjectOne.class).entries();

    Log.w(TAG, "Retrieved query -- " + result);

    List<String> keys = store.query().keyPrefix("com").anyType().keys();

    Log.w(TAG, "Retrieved keys -- " + keys);

    List<SampleObjectOne> values =
        store.query().keyPrefix("com").type(SampleObjectOne.class).values();

    Log.w(TAG, "Retrieved values -- " + values);

    //add gists
    try {
      InputStream stream = getAssets().open("gists.json");
      BufferedSource source = Okio.buffer(Okio.source(stream));

      List<Gist> gists = new ArrayList<>();
      processGists(JsonReader.of(source), gists);
      for (Gist gist : gists) {
        eve.store().set(Gist.KEY_PREFIX + gist.id(), gist);
      }
      List<Gist> gistValues = store.query().keyPrefix(Gist.KEY_PREFIX).type(Gist.class).values();

      Log.w(TAG, "Retrieved gist values -- size: " + gistValues.size() + " " + gistValues);

      List<String> gistKeys = store.query().keyPrefix(Gist.KEY_PREFIX).type(Gist.class).keys();
      Log.w(TAG, "Retrieved gist keys -- size: " + gistKeys.size() + " " + gistKeys);
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<String> gistKeys = store.query().type(Gist.class).keys();

    Log.w(TAG, "Retrieved gist keys of type -- size: " + gistKeys.size() + " " + gistKeys);

    setCount(R.string.db_count, sqlStore.count() + "");
  }

  /**
   * Process Json and create a list of gists.
   */
  private void processGists(JsonReader reader, List<Gist> gists) throws IOException {

    JsonReader.Token token = reader.peek();
    switch (token) {
      case BEGIN_ARRAY: {
        reader.beginArray();
        break;
      }
      case BEGIN_OBJECT: {
        Gist gist = moshi.adapter(Gist.class).fromJson(reader);
        gists.add(gist);
        break;
      }
      case END_OBJECT: {
        reader.endObject();
        break;
      }
      case END_ARRAY: {
        reader.endArray();
        return;
      }
      case END_DOCUMENT: {
        return;
      }
      default: {
        //no-op
      }
    }
    processGists(reader, gists);
  }

  private void setDbName(@StringRes int resId, String... args) {
    ((TextView) findViewById(R.id.txt_db_name)).setText(getString(resId, args));
  }

  private void setDbSize(@StringRes int resId, String... args) {
    ((TextView) findViewById(R.id.txt_db_size)).setText(getString(resId, args));
  }

  private void setCount(@StringRes int resId, String... args) {
    ((TextView) findViewById(R.id.txt_count)).setText(getString(resId, args));
  }

  @Override protected void onDestroy() {
    Log.d(TAG, "onDestroy: " + eve.store().clear());
    super.onDestroy();
  }
}
