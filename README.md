# apidoc-preprocessor-java

apidoc is a great documentation tool for RESTful web APIs. Unfortunately it doesn't scale: as the number of endpoints grow, keeping an up-to-date snapshot becomes a burden.

This burden is due to the overhead of making sure that a change in the code is replicated in the docs. However, for a big part of the docs, the permanent source of truth is (and should be) the source code.

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

(*) are interfaces and are agnostic of the frameworks in the java code
(**) is also an interface and is agnostic of the apidoc engine

Possible implementations are:
scanner: SpringScanner, JerseyScanner
model: SpringModel, JerseyModel
preprocessor: ApidocPreprocessor, ...
