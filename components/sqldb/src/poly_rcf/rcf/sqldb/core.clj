;; Copyright (C) 2022, Doctor Evidence. All rights reserved.

(ns poly-rcf.rcf.sqldb.core
  "SQL DB client impl."
  {:authors ["Jozef Wagner"]}
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [next.jdbc.result-set :as rs]
            [next.jdbc.transaction :as transaction]
            [clojure.tools.logging :refer [info warn trace]])
  (:import [com.zaxxer.hikari HikariDataSource]))


;;; HikariCP

;;; HugSQL

(defn get-connection
  [sqlconn]
  (or (:hikari-datasource sqlconn) sqlconn))

(defn strip-update-count
  [result]
  (if (and (seqable? result)
           (= 1 (count result))
           (empty? (dissoc (first result) :next.jdbc/update-count))
           (integer? (:next.jdbc/update-count (first result))))
    [(:next.jdbc/update-count (first result))]
    result))

;;; SQLDB

;;; System Component

(defn start-component
  [new-sqldb]
  (info "Create component with db" new-sqldb)
  (let [db-spec (:db-spec new-sqldb)
        hikari-datasource (connection/->pool HikariDataSource db-spec)]
    (assoc new-sqldb
           :hikari-datasource hikari-datasource)))

(defn stop-component
  [sqldb]
  (when-let [^HikariDataSource ds (:hikari-datasource sqldb)]
    (when-not (.isClosed ds)
      (.close ds)))
  (dissoc sqldb :hikari-datasource))

(defn suspend-component
  [sqldb]
  (info "Suspending SQLDB component")
  sqldb)

(defn resume-component
  [_new-sqldb suspended-sqldb]
  ;; TODO: Proper reconnect when cfg changes
  (info "Resuming SQLDB component")
  suspended-sqldb)

(defn new-component
  "Returns new sqldb system component."
  [cfg tag]
  (let [db-config (get-in cfg [:dre tag])]
    (-> db-config
        (with-meta {'com.stuartsierra.component/start start-component
                    'com.stuartsierra.component/stop stop-component
                    'suspendable.core/suspend suspend-component
                    'suspendable.core/resume resume-component}))))

(comment

  (set! *warn-on-reflection* true)
)
