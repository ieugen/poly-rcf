(ns poly-rcf.rcf.sqldb.interface
  (:require [poly-rcf.rcf.sqldb.core :as core]))


(defn new-component
  "Returns new sqldb system component."
  [cfg tag]
  (core/new-component cfg tag))