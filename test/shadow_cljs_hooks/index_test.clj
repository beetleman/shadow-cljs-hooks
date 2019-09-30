(ns shadow-cljs-hooks.index-test
  (:require [shadow-cljs-hooks.index :as sut]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [clojure.test :as t :refer [deftest]]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]))


(defspec test-check-conform-options
  100
  (prop/for-all [build-state (s/gen ::hooks.spec/build-state)
                 extra-options (s/gen ::sut/options)]
                (let [options (sut/conform-options build-state extra-options)]
                  (and (set/subset? (set extra-options)
                                    (set options))
                       (s/valid? ::sut/options
                                 options)))))

(deftest test-enty-point-js
  (t/testing "no porovided entry point"
    (t/is (= sut/entry-point-js-error-message
             (sut/entry-point-js {}))))
  (t/testing "entry point provided: `:foo.bar/baz`"
    (t/is (= "foo.bar.baz();"
             (sut/entry-point-js {:entry-point 'foo.bar/baz})))))

(defspec test-check-enty-point-js
  100
  (prop/for-all [options (s/gen ::sut/options)]
                (string? (sut/entry-point-js options))))

(defspec test-check-template
  100
  (prop/for-all [options (s/gen ::sut/options)
                 build-state (s/gen ::hooks.spec/build-state)]
                (string? (sut/template "js/main.js"
                                       (sut/conform-options build-state
                                                            options)))))

(defmacro on-flush? [hook]
  `(= (:shadow.build/stage (meta #'~hook))
      :flush))


(t/deftest test-hook
  (t/testing "index hook run on flush"
    (t/is (on-flush? sut/hook))))
