(ns shadow-cljs-hooks.fulcro-css-test
  (:require [clojure.test :as t :refer [deftest]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [com.fulcrologic.fulcro-css.localized-dom-server :as dom]
            [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [shadow-cljs-hooks.fulcro-css :as sut]
            [shadow-cljs-hooks.test.util :as test.util]
            [clojure.test.check.properties :as prop]
            [clojure.spec.alpha :as s]
            [shadow-cljs-hooks.spec :as hooks.spec])
  (:import java.io.File))

(defsc UserName [_ {::keys [name]}]
  {:css [[:.root {:color :red}]]
   :query [::name]}
  (dom/p name))

(def ui-user-name (comp/factory UserName))

(defsc MainComponent [_ {::keys [user-name]}]
  {:css [[:.root {:background :blue}]]
   :query [{::user-name (comp/get-query UserName)}]}
  (dom/div :.root
           (ui-user-name)))

(defspec test-check-hook*
  10
  (prop/for-all [build-state (s/gen ::hooks.spec/build-state)
                 options (s/gen ::sut/options)]
                (s/valid? ::hooks.spec/build-state
                          (sut/hook* build-state
                                     (assoc options :component `MainComponent)
                                     {:write-css (fn [css output-dir file-name]
                                                   (assert (string? css)
                                                           "css should be string")
                                                   (assert (instance? java.io.File
                                                                      output-dir)
                                                           "output-dir should be string")
                                                   (assert (string? file-name)
                                                           "file-name should be string"))}))))

(deftest test-generate
  (let [css (sut/generate `MainComponent)]
    (t/testing "check if generated css contains UserName css"
      (t/is (re-matches #".+UserName__root\{color:red\}.*"
                        css)))
    (t/testing "check if generated css contains MainComponent css"
      (t/is (re-matches #".+MainComponent__root\{background:blue\}.*"
                        css)))))

(t/deftest test-hook
  (t/testing "fulcro-css hook run on flush"
    (t/is (test.util/on-flush? sut/hook))))
