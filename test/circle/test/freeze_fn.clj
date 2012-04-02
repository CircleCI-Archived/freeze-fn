(ns circle.test.freeze-fn
  (:require [circle.freeze-fn :as fn])
  (:use [clojure.test]))

(deftest freeze-implements-ifn
  (let [f (fn/save #'inc 3)]
    (is (= 4 (f)))))

(deftest freeze-implements-callable
  (let [f (fn/save #'inc 3)]
    (is (= 4 (.call f)))))

(deftest freeze-implements-runnable
  (let [f (fn/save #'inc 3)]
    (is (= nil (.run f)))))