package uncmn.eve.sample;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Complex object resource.
 */
@AutoValue public abstract class SampleObjectTwo {
  public static final String CONVERTER_KEY = "SampleObjectTwo";

  public abstract String name();

  public abstract int[] items();

  public abstract SampleObjectOne objectOne();

  public static SampleObjectTwo create(String name, int[] items, SampleObjectOne objectOne) {
    return new AutoValue_SampleObjectTwo(name, items, objectOne);
  }

  public static JsonAdapter<SampleObjectTwo> jsonAdapter(Moshi moshi) {
    return new AutoValue_SampleObjectTwo.MoshiJsonAdapter(moshi);
  }
}
