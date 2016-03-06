###AsyncTaskを用いた画像ダウンロード
最近のAndroidではメインスレッドでネットワーク通信を行うことを禁止している<br>
なので非同期で画像をダウンロードさせる<br>
主にAsyncTask<Params, Progress, Result>を利用している<br>
