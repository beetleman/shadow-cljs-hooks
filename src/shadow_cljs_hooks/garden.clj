(ns shadow-cljs-hooks.garden
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [garden.core :as garden]
            [shadow-cljs-hooks.css :as hooks.css]
            [shadow-cljs-hooks.spec :as hooks.spec]
            [shadow-cljs-hooks.symbols :as symbols]))

(defn generate
  ([css-symbol]
   (generate css-symbol {:pretty-print? false}))
  ([css-symbol garden-flags]
   (garden/css garden-flags (symbols/eval-symbol css-symbol))))

(s/def ::css symbol?)
(s/def ::output-dir ::hooks.spec/not-empty-string)
(s/def ::asset-path ::hooks.spec/not-empty-string)
(s/def ::options (s/keys :req-un [::output-dir ::asset-path ::css]
                         :opt-un [::hooks.css/garden-flags]))


(defn hook* [build-state
             options
             {:keys [write-css]}]
  {:pre [(hooks.spec/valid? ::hooks.spec/build-state build-state)
         (hooks.spec/valid? ::options options)]}
  (let [{:keys [output-dir asset-path css]} options
        output-dir                          (io/file output-dir)
        file-name                           "main.css"]
    (write-css (generate css)
               output-dir
               file-name)
    (hooks.css/assoc-path build-state (str asset-path "/" file-name))))

(defn hook
  {:shadow.build/stage :flush}
  ([build-state]
   (hook build-state {}))
  ([build-state options]
   (hook* build-state
          options
          {:write-css hooks.css/write!})))
