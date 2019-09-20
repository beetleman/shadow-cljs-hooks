(ns shadow-cljs-hooks.index-test
  (:require [shadow-cljs-hooks.index :as sut]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [clojure.test :as t :refer [deftest]]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.spec.alpha :as s]))

(defspec test-conform-options
  100
  (prop/for-all [build-state (s/gen ::hooks.spec/build-state)]
                (s/valid? ::sut/options
                          (sut/conform-options build-state {:entry-point 'app.main/init}))))
