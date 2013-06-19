本ひな形作成までの手順
=============

# プロジェクトの作成
```
$ play new PlayTemplate
```

## application.confのコピー

　application.confには将来的にDBのパスワードを書き込んだりするので，共有しない．
そのため，_baseファイルを作って，設定の変更はこちらにも反映して共有する．

```
$ cd PlayTemplate
$ cp conf/application.conf conf/application.conf_base
```