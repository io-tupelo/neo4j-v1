(ns tst.neo4j.core
  (:use neo4j.core tupelo.core tupelo.test)
  (:require
    [environ.core :as environ]
    [tupelo.string :as str]
  )
  (:import
    [java.net URI]
    [org.neo4j.driver.exceptions TransientException]
  ))

(dotest 
  (newline)
  (println :-----------------------------------------------------------------------------)
  (spy (str "neo4j-uri  =>  " (environ/env :neo4j-uri)))
  (newline)
)

(defonce dbconn
  (connect
    (URI. (environ/env :neo4j-uri))
    (environ/env :neo4j-username)
    (environ/env :neo4j-password)))

;-----------------------------------------------------------------------------
(defquery create-test-user
          "CREATE (u:TestUser $user)-[:SELF {reason: \"to test\"}]->(u)")

(defquery get-test-users-by-name
          "MATCH (u:TestUser {name: $name}) RETURN u.name as name, u.role as role, u.age as age, u.smokes as smokes")

(defquery get-test-users-relationship
          "MATCH (u:TestUser {name: $name})-[s:SELF]->() RETURN collect(u) as ucoll, collect(s) as scoll")

(defquery delete-test-user-by-name
          "MATCH (u:TestUser {name: $name}) DETACH DELETE u")

(def dummy-user {:name "MyTestUser" :role "Dummy" :age 42 :smokes true})

(def name-lookup {:name (:name dummy-user)})

;-----------------------------------------------------------------------------
; Simple CRUD
(dotest ; create-get-delete-user
  (with-open [session (get-session dbconn)]
    (spy "You can create a new user with neo4j")
    (time (create-test-user session {:user dummy-user}))

    (spy "You can get a created user by name")
    (time
      (is (= (get-test-users-by-name session name-lookup)
             (list dummy-user))))

    (spy "You can get a relationship")
    (time
      (is (= (first (get-test-users-relationship session name-lookup))
             {:ucoll (list dummy-user) :scoll (list {:reason "to test"})})))

    (spy "You can remove a user by name")
    (time
      (delete-test-user-by-name session name-lookup))

    (spy "Removed users can't be retrieved")
    (time
      (is (= (get-test-users-by-name session name-lookup)
             (list))))
  ))

; Cypher exceptions
(dotest ; invalid-cypher-does-throw
  (with-open [session (get-session dbconn)]
    (spy "An invalid cypher query does trigger an exception")
    (time (is (thrown? Exception (execute session "INVALID!!§$/%&/("))))
  ))

; Transactions
(dotest ; transactions-do-commit

  (spy "If using a transaction, writes are persistet")
  (time
    (with-transaction dbconn
                      tx
                      (execute tx "CREATE (x:test $t)" {:t {:payload 42}})))

  (spy "If using a transaction, writes are persistet")
  (time
    (with-transaction dbconn
                      tx
                      (is (= (execute tx "MATCH (x:test) RETURN x")
                             '({:x {:payload 42}})))))

  (spy "If using a transaction, writes are persistet")
  (time
    (with-transaction dbconn
                      tx
                      (execute tx "MATCH (x:test) DELETE x" {:t {:payload 42}})))

  (spy "If using a transaction, writes are persistet")
  (time
    (with-transaction dbconn
                      tx
                      (is (= (execute tx "MATCH (x:test) RETURN x")
                             '()))))
)

; Retry
(dotest ; deadlocks-fail
  (newline)
  (spy "When a deadlock occures,")

  (spy "the transaction throws an Exception")
  (time
    (is (thrown? TransientException
                 (with-transaction dbconn
                                   tx
                                   (throw (TransientException. "" "I fail"))))))
  (spy "the retried transaction works")
  (time
    (let [fail-times (atom 3)]
      (is (= :result
             (with-retry [dbconn tx]
                         (if (pos? @fail-times)
                           (do (swap! fail-times dec)
                               (throw (TransientException. "" "I fail")))
                           :result))))))

  ; ***** SLOW!!!  500ms local Neo4j, 1.5 sec with remote Aura Neo4j *****
  ; ***** Was 150 sec with original default max-retries=1000 (reduced => 10) *****
  (when true
    (spy "the retried transaction throws after max retries")
    (time
        (is (thrown? TransientException
                     (with-retry [dbconn tx]
                                 (throw (TransientException. "" "I fail")))))))
)
