(ns shadow-cljs-hooks.core-test
  (:require [shadow-cljs-hooks.core :as sut]
            [clojure.test :as t]))


(defmacro on-flush? [hook]
  `(= (:shadow.build/stage (meta #'~hook))
      :flush))


(t/deftest test-index
  (t/testing "index run on flush"
    (t/is (on-flush? sut/index))))
