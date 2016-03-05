package jp.nomunomu.asyncimagedownload;

import android.graphics.Bitmap;

/*
 * コールバックされる関数を定義するInterface AsyncImageDownloadDelegate
 * 別クラスに非同期処理で得たObjectを渡すためのInterface
 * 主にメインクラスにimplementsして使う。implementsすることにより、
 * AsyncImageDownloadDelegateクラスの関数がメインクラスでOverrideされ、
 * 非同期処理クラスから呼び出すことによりメインクラスでOverrideされた関数を呼び出すことができる
 */
public interface AsyncImageDownloadDelegate {

    public void SucceedDownloadImage(Bitmap result);
    public void StartDownloadImage();
}
