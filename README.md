# restapi-preprocessor-java

There's a lot of documentation frameworks for Restful web services to choose from: swagger.io, apidocjs.com to name a few. These frameworks are opinionated in that they guide the user by enforcing a format, from which a view of the API layer is generated in HTML.

This short-sighted solution immediately entails the problem of having to deal with two views of the API (at the minimum). The first is the view as represented by the source code while the second is the framework's.

We believe the approach of these frameworks in fundamentally wrong. There should be one source of truth for the API and it should be the source code. Since the latter captures both the specification and implementation of the API layer, extracting the specification is possible. Doing the reverse however is not: generating the source code from, say, a swagger specification will still require the developer to paste the implementation. Doing so every time the specs has changes is not computer science.

However, in order not to reinvent the wheel, we can regard swagger et al as incomplete solutions and prepend it with a preprocessor layer that maps the source code the their respective inputs.

STOPPED HERE
We can thus split the docs in two categories: hardcoded vs generated

For example:

	/**
	 * @apiDescription text
	 */

should be input (hardcoded). It can, in a Java context, double as a javadoc description of the method it annotates. The following however:

	/**
	 * @api {method} path [title]
	 * @apiSuccess [(group)] [{type}] field [description]
	 * @apiError [(group)] [{type}] field [description]
	 */

should be generated and if not will cause duplication. Again, in a Java context, Reflection can be leveraged to retrieve the underlying raw data and format it to conform to apidoc.

This (repo) preprocessor targets web services written in Java. It uses a pluggable architecture for the entrypoint. For now, the Spring entrypoint is being worked on. A future candidate may be Jersey, depending on the need and the available resources for this project.

# Architecture

    #############      ############      ##########      ##################      #################      ##########      ########
    #           #      #          #      #        #      #                #      #               #      #        #      #      #
    # java code # ---> # scanner* # ---> # model* # ---> # preprocessor** # ---> # docs template # ---> # engine # ---> # docs #
    #           #      #          #      #        #      #                #      #               #      #        #      #      #
    #############      ############      ##########      ##################      #################      ##########      ########

* are interfaces and are agnostic of the frameworks in the java code

** is also an interface and is agnostic of the apidoc engine

Possible implementations are:
scanner: SpringScanner, JerseyScanner
model: SpringModel, JerseyModel
preprocessor: ApidocPreprocessor, ...
