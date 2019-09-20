(ns shadow-cljs-hooks.index-test
  (:require [shadow-cljs-hooks.index :as sut]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [clojure.test :as t]
            [clojure.spec.alpha :as s]))


(t/deftest test-conform-options
  ;;TODO: use test.check
  (let [build-state (-> (s/exercise ::hooks.spec/build-state 1) first first)]
    (t/is (s/valid? ::sut/options
                    (sut/conform-options build-state {:entry-point 'app.main/init})))))
