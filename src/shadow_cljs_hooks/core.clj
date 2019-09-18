(ns shadow-cljs-hooks.core
  (:require [shadow-cljs-hooks.index :as hooks.index]))


(defn index
  {:shadow.build/stage :flush}
  ([build-state]
   (index build-state {}))
  ([build-state options]
   (hooks.index/write-html build-state options)
   build-state))
