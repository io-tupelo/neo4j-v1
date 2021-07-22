(defproject tupelo/neo4j "0.1.0"
  :description "Clojure bindings for Neo4j using the Java driver"

  :dependencies [[org.neo4j.driver/neo4j-java-driver "4.1.1"]
                 [clj-time "0.15.2"]
                 [tupelo "21.07.08"]
                 ]


  :profiles {:provided     {:dependencies [[org.clojure/clojure "1.10.3"]
                                           [joplin.core "0.3.11"]
                                           [org.neo4j.test/neo4j-harness "4.0.0"]]}
             :default      [:base :system :user :provided :dev]
             :dev          [:project/dev :profiles/dev]
             :profiles/dev {}
             :project/dev  {:jvm-opts     ["-Dclojure.spec.check-asserts=true"
                                           "-XX:-OmitStackTraceInFastThrow"]
                            :dependencies []}}

  )
