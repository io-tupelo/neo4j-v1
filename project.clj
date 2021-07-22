(defproject tupelo/neo4j "0.1.0"
  :description "Clojure bindings for Neo4j using the Java driver"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [
    [com.jakemccrary/lein-test-refresh "0.24.1"]
    [lein-ancient "0.7.0"]
    ]

  :dependencies [[org.neo4j.driver/neo4j-java-driver "4.3.3"]
                 [clj-time "0.15.2"]
                 [prismatic/schema "1.1.12"]
                 [tupelo "21.07.08"]
                 ]

  :profiles {:provided     {:dependencies [[org.clojure/clojure "1.10.3"]
                                           [joplin.core "0.3.11"]
                                           [org.neo4j.test/neo4j-harness "4.0.0"]]}
             :profiles/dev {}
             :project/dev  {:jvm-opts     ["-Dclojure.spec.check-asserts=true"
                                           "-XX:-OmitStackTraceInFastThrow"]
                            :dependencies []}}

             :dev          [:project/dev :profiles/dev]
             :default      [:base :system :user :provided :dev]

  )


;  :global-vars {*warn-on-reflection* false}
;  :main ^:skip-aot neo4j.core
;
;  :source-paths            ["src/clj"]
;  :java-source-paths       ["src/java"]
;  :test-paths              ["test/clj"]
;  :target-path             "target/%s"
;  :compile-path            "%s/class-files"
;  :clean-targets           [:target-path]
;
;  :profiles {:dev     {:dependencies [ ]}
;             :uberjar {:aot :all}}
;
;  :jvm-opts ["-Xms500m" "-Xmx2g" ]
