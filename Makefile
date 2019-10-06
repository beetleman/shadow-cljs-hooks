.PHONY: test

test:
	clojure -A:dev -m kaocha.runner
deploy:
	mvn deploy
