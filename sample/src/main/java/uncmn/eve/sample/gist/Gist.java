package uncmn.eve.sample.gist;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * An object instance representing Gist.
 * "url": "https://api.github.com/gists/d2de528c814ffe9d5a8adc0de10a9627",
 * "forks_url": "https://api.github.com/gists/d2de528c814ffe9d5a8adc0de10a9627/forks",
 * "commits_url": "https://api.github.com/gists/d2de528c814ffe9d5a8adc0de10a9627/commits",
 * "id": "d2de528c814ffe9d5a8adc0de10a9627",
 * "git_pull_url": "https://gist.github.com/d2de528c814ffe9d5a8adc0de10a9627.git",
 * "git_push_url": "https://gist.github.com/d2de528c814ffe9d5a8adc0de10a9627.git",
 * "html_url": "https://gist.github.com/d2de528c814ffe9d5a8adc0de10a9627",
 * "files": {
 * "Lucky": {
 * "filename": "Lucky",
 * "type": "text/plain",
 * "language": null,
 * "raw_url": "https://gist.githubusercontent.com/anonymous/d2de528c814ffe9d5a8adc0de10a9627/raw/bd99d6ca10f341245612614bc57ea63a6de1a6eb/Lucky",
 * "size": 404
 * }
 * },
 * "public": true,
 * "created_at": "2016-06-04T21:53:25Z",
 * "updated_at": "2016-06-04T21:53:25Z",
 * "description": "One moment, please <a href=\" http://www.victor.co.uk/nexium-alternatives-generic.pdf
 * \">nexium card discount</a>  It will be the fourth time the group has tried to go public, after
 * three attempts failed in the last 19 years due to opposition from within the governing majority,
 * which feared an electoral backlash from tampering with a revered institution whose red
 * post-boxes
 * are known around the world.\n ",
 * "comments": 0,
 * "user": null,
 * "comments_url": "https://api.github.com/gists/d2de528c814ffe9d5a8adc0de10a9627/comments",
 * "truncated": false
 */

@AutoValue public abstract class Gist {

  public static final String KEY_PREFIX = "gist";

  public static final String CONVERTER_KEY = Gist.class.getSimpleName();

  public static JsonAdapter<Gist> moshiAdapter(Moshi moshi) {
    return new AutoValue_Gist.MoshiJsonAdapter(moshi);
  }

  @NonNull @Json(name = "url") public abstract String url();

  @NonNull @Json(name = "forks_url") public abstract String forksUrl();

  @NonNull @Json(name = "commits_url") public abstract String commitsUrl();

  @NonNull @Json(name = "id") public abstract String id();

  @NonNull @Json(name = "git_pull_url") public abstract String gitPullUrl();

  @NonNull @Json(name = "git_push_url") public abstract String gitPushUrl();

  @NonNull @Json(name = "html_url") public abstract String htmlUrl();

  @Json(name = "public") public abstract boolean isPublic();

  @NonNull @Json(name = "created_at") public abstract String createdAt();

  @Nullable @Json(name = "updated_at") public abstract String updatedAt();

  @Nullable @Json(name = "description") public abstract String description();

  @Json(name = "comments") public abstract int comments();

  @Nullable @Json(name = "user") public abstract String user();

  @Nullable @Json(name = "comments_url") public abstract String commentsUrl();

  @Json(name = "truncated") public abstract boolean truncated();
}
