(ns shadow-cljs-hooks.test.util)

(defmacro on-flush? [hook]
  `(= (:shadow.build/stage (meta #'~hook))
      :flush))
