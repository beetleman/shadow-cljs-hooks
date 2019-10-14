(ns shadow-cljs-hooks.garden-test
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [shadow-cljs-hooks.garden :as sut]
            [shadow-cljs-hooks.test.util :as test.util]
            [clojure.test.check.properties :as prop]
            [clojure.spec.alpha :as s]
            [shadow-cljs-hooks.spec :as hooks.spec])
  (:import java.io.File))

(def css [[:.root {:color :red}
           [:.user-name {:background :blue}]]])

(defspec test-check-hook*
  10
  (prop/for-all [build-state (s/gen ::hooks.spec/build-state)
                 options (s/gen ::sut/options)]
                (s/valid? ::hooks.spec/build-state
                          (sut/hook* build-state
                                     (assoc options :css `css)
                                     {:write-css (fn [css output-dir file-name]
                                                   (assert (string? css)
                                                           "css should be string")
                                                   (assert (instance? java.io.File
                                                                      output-dir)
                                                           "output-dir should be string")
                                                   (assert (string? file-name)
                                                           "file-name should be string"))}))))

(deftest test-generate
  (let [css (sut/generate `css)]
    (t/testing "check if generated css contains UserName css"
      (t/is (re-matches #".+root\{color:red\}.*"
                        css)))
    (t/testing "check if generated css contains MainComponent css"
      (t/is (re-matches #".+user-name\{background:blue\}.*"
                        css)))))

(t/deftest test-hook
  (t/testing "fulcro-css hook run on flush"
    (t/is (test.util/on-flush? sut/hook))))
