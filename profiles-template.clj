; ***** if using local Neo4j Desktop *****
{:profiles/dev  {:env
                   {:neo4j-uri      "bolt://localhost:7687"
                    :neo4j-username "neo4j"
                    :neo4j-password "secret"}}
 :profiles/test {:env
                   {:neo4j-uri      "bolt://localhost:7687"
                    :neo4j-username "neo4j"
                    :neo4j-password "secret"}}}

; ***** if using Neo4j Aura *****
; {:profiles/dev  {:env
;                    {:neo4j-uri      "neo4j+s://abcd1234.databases.neo4j.io"
;                     :neo4j-username "neo4j"
;                     :neo4j-password "some-random-password-string"}}
;  :profiles/test {:env
;                    {:neo4j-uri      "neo4j+s://abcd1234.databases.neo4j.io"
;                     :neo4j-username "neo4j"
;                     :neo4j-password "some-random-password-string"}}}
