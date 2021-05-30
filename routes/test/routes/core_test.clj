(ns routes.core-test
  (:require [clojure.test :refer :all]
            [routes.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest line->routes-db-loop
  (line->routes [] )
  )