var merohcControllers = angular.module('merohc.controllers', ['ui.router']);

merohcControllers.controller('HomePageResumeController', ['$scope', '$http', 'merohc-config', function ($scope, $http, merohcConfig) {

  $scope.companiesNumber=10;

  $scope.personNumber=10;

  $scope.invoiceNumber=10;

  var baseUrl = merohcConfig.BASE_URL

  $http.get(baseUrl + '/company').success(function(data){
    $scope.companiesNumber = data.length;
  });

  $http.get(baseUrl + '/contact').success(function(data){
    $scope.personNumber = data.length;
  });

  $http.get(baseUrl + '/invoice').success(function(data){
    $scope.invoiceNumber = data.length;
  });

}]);

merohcControllers.controller('HomePageCalendarController', function ($scope) {

    $scope.date=new Date();
    $scope.getSaints = function(){
      var month = $scope.date.getMonth();
      var day = $scope.date.getDate()-1;

      return $scope.saintsData[month][day];

    };

    $scope.saintsData= [
        ["MARIE", "BASILE, VASSILI", "GENEVIEVE, GINETTE", "EDDY, ODILON", "EDOUARD, EMILIENNE, TED, TEDDY", "MELAINE, TIPHAINE", "CEDRIC, RAYMOND, RAYMONDE, VIRGINIE", "LUCIEN, LUCIENNE, PEGGY", "ALEXIA, ALIX", "BILL, BILLY, GUILLAUME, GUILLEMETTE, WILLIAM, WILLY", "PAULIN", "AILRED, CESARINE, EILRED, TANIA, TANIS, TATIANA", "HILAIRE, YVETTE", "NINA", "AMAURY, MAUR, RACHEL, REMI, REMY", "HONORAT, MARCEAU, MARCEL", "ANTHONY, ANTOINE, ROSELINE", "GWENDAL, PRISCA", "BREVELAER, MARIO, MARIUS", "BASTIEN, FABIEN, FABIENNE, SEBASTIEN", "AGNES, INES", "VINCE, VINCENT", "BARNARD", "FRANCOIS DE SALES, PACO, TIM", "APOLLOS, MORGANE", "MELANIE, PAOL, PAOLA, PAULA, PAULE, PAULINE, TIMOTHEE", "ANGELE, ANGELINE, ANGELIQUE", "THOMAS D'AQUIN", "GILDAS", "BATHILDE, BATHYLLE, BOB, JACINTHE, MARTINE", "MARCELLE, NIKITA"],
        ["ELLA, ELLE, ELLENITA, VIRIDIANA", "GUTUAL, THEOPHANE", "ANATOLE, BLAISE, OSCAR", "BERENICE, GIL, GILBERT, VANESSA, VERONIQUE", "AGATHE, AVIT", "AMAND, AMANDA, DOROTHEE, GASTON", "EUGENIE", "ELISENDA, JACKIE, JACQUELINE", "APPOLLINE", "ARNAUD", "N.-D. DE LOURDES, N.D. DE LOURDES", "FELIX", "BEA, BEATRICE, JORDAN, JORDANNE", "TINO, VALENTIN", "CLAUDE, CLAUDIE, CLAUDINE, CLAUDIUS, FAUSTIN", "ELIAZ, JULIENNE, LUCILLE, PAMELA, PAMPHILE", "ALEXIS, LOGAN", "BERNADETTE, FLAVIEN, NADETTE, NADINE, SIMEON", "GABIN", "AIMEE", "DAMIEN, DINAN", "ISA, ISABELLE, TERRY", "LAZARE, POLYCARPE", "MODESTE", "ROMEO", "NESTOR", "HONORINE", "ANTOINETTE, ROMAIN", "AUGUSTE"],
        ["ALBIN, ALBINA, AUBIN, DENIEL, JONATHAN", "CARL, CHARLES LE B., JAOUEN, JOEVIN", "GUENOLE", "CASIMIR", "GERAN, OLIVE, OLIVETTE, OLIVIA", "COLETTE, NICOLE", "FELICIE, FELICITE, PERPETUE", "JEAN DE D.", "FRANCE, FRANCETTE, FRANCINE, FRANCOISE, PAQUITA", "ANASTASIE, VIVIEN", "ROSINE", "ELPHEGE, JUSTINE", "RODRIGUE", "MATHILDE, MAUD", "LOUISE", "BENEDICTE", "PATRICE, PATRICIA, PATRICK, PATSY, PATTY", "CYRIL, CYRILLE, SALVATORE", "JOSE, JOSEPH, JOSEPHINE, JOSETTE, JOSIANE", "HERBET, SVETLANA, WULFRAN", "CLEMENCE", "LEA, LEILA, LIA, LILA", "VICTORIEN", "CATHERINE DE SUEDE, KARINE, KETTY", "ANNONCIATION", "LARA, LARISSA", "HABIB", "GONTRAN", "GLADYS", "AMEDEE", "AMOS, BABINE, BALBINE, BEN, BENJAMIN, BENJAMINE"],
        ["HUGUES, HUGUETTE, VALERY", "SANDIE, SANDRA, SANDRINE, SANDY", "DICK, RICHARD", "ALETHE, ALETTE, ISIDORE, MAYA", "IRENE", "MARCELLIN", "J.B. DE LA SALLE", "CONSTANCE, JULIE", "GAUTHIER, GAUTIER, WALTER", "FULBERT", "STAN, STANISLAS", "JULES", "IDA", "LIDWINE, MAXIME", "PATERNE", "BENOIT-JOSEPH", "ETIENNE", "GRETA, PARFAIT", "EMMA", "GIRAUD, ODETTE", "ALSELME, SELMA", "ALEX, ALEXANDRA, ALEXANDRE", "FORTUNAT, GEORGES, GEORGETTE, GEORGINA, YOURI", "FIDELE, MARJORIE", "MARC", "ALIDA", "ZITA", "VALERIE", "CATHERINE, CATHERINE DE SIENNE", "MARIEN, ROBERT, ROSEMONDE"],
        ["BRIEG, JEREMIE, JEREMY, MUGUET, MUGUETTE, TAMARA", "ANTONIN, ATHANASE, BORICE, BORIS, ZOE", "GURVAN, JACQUES, PHILIPPE", "FLORIAN, SYLVAIN, SYLVAINE", "ANGE, JUDITH", "CALINE, PRUDENCE", "DOMITILLE, FLAVIE, GISELE", "DESIRE, DUNVEL, JEANNE, JOHANNA", "ISAIE, PACOME", "SOLANGE", "ESTELLE, ETOILE, MAYEUL, STELLA", "ACHILLE, ANAIS", "MAELA, ROLANDE", "MATHIAS", "DENISE, PRIMAEL, VICTORIN", "ENORA, HONORE", "PASCAL, PASCALE", "CORALIE, CORINNE, ERIC, ERICK, ERIKA, JULIETTE", "CELESTIN, ERWAN, ERWIN, YVAN, YVES, YVON, YVONNE", "BERNARDIN, ROXANE", "CHARLES, CHARLY, CONSTANTIN", "EMILE, MILOUD, RITA", "DIDIER", "AMAEL, DONATIEN, MAEL, MAËLE, ROGATIEN", "SOPHIE", "BERENGER, BERENGERE", "AUGUSTIN DE C.", "GERMAIN, JORAN", "AYMAR", "FERDINAND, JEANNE D'ARC, JEANNINE, JOHANNA, JOHANNE, JUANITA, LORAINE", "PERINNE, PERNELLE, PERRETTE, PERRINE, PETRONILLE, PIERRETTE"],
        ["JUSTIN, ROMAN, RONAN", "BLANDINE, VITAL", "KEVIN", "CLOTHIDE, CLOTILDE", "BONIFACE, IGOR", "CLAUDE, CLAUDIE, CLAUDINE, CLAUDIUS, GOAL, GURVAL, NORBERT", "GIL, GILBERT, MAITE, MARIE-THÉRÈSE, MERIADEC", "ARMAND, MEDARD, PRUNE", "DIANA, DIANE, FELICIEN", "GLORIA, LANDRY", "BARNABE, YOLANDE", "GUY, GUYONNE", "ANTHONY, ANTOINE, ANTONIO, TONY", "CANDICE, ELISEE, VALÈRE", "GERMAINE", "ARGAN, AURELIEN, FERREOL, REGIS", "HERVE, RAINIER", "LEONCE", "GERVAIS, GERVAISE, MICHELINE, ROMUALD", "SILVERE", "ALOYSE, GINA, GONZAGUE, LOIS, RALPH, RAOUL, RODOLPHE, RUDY", "ALBAN, ALBANE, ALBANNIE, ALBE, THOMAS", "AUDREY", "BAPTISTE, GIAN-LUCA, JEAN, JEAN PHILIPPE, JEAN-BAPTISTE, JOANNY, YANN, YOANN", "ELEONORE, LORE, NORA, PROSPER, SALOMON", "ANTHELME, OLYMPE", "FERNAND, FERNANDE", "IRENEE, MIRKO, PIERRICK", "PABLO, PAUL, PETER, PIERRE", "ADOLPHE, ANDREIA, MARTIAL"],
        ["DIRCK, DIRK, ESTHER, SERVAN, SERVANE, THIERRY", "MARTINIEN", "THOMAS, TOM", "BERTHE, ELIANE, FLORENT", "ANTOINE, TONIO, TONY", "MARIETTA, MARIETTE, NOLWENN", "ELIANE", "EDGAR, KILLIAN, THIBAULT, THIBAUT, THIEBAUD", "AMANDINE, HERMINE, IRMA, MARIANNE", "ULRICH", "BENOIT, OLGA", "JASON, OLIVIER, OLLIE", "CLELIA, ENRIQUE, EUGENE, HARRY, HENRI, JOEL, JOELLE", "CAMILLE, ELISENDA", "BONAVENTURE, DON, DONALD, WLADIMIR", "CARMEN, ELVIRE", "ARLETTE, CAROLE, CAROLINE, CHARLINE, CHARLOTTE, MARCELLINE", "ARNOULD, FREDDY, FREDERIC, FREDERIQUE", "ARSENE, MADEL", "ELIE, ELIETTE, HELYETTE, MARGUERITE, MARINA, MARINE, MARJORIE", "VICTOR", "MADDY, MADELEINE, MAGALI, MAGGY, MAIALEN, MARIE-MADELEINE", "BRIGITTE", "CHRISTEL, CHRISTELLE, CHRISTINE, CRISTELLE, IVAN, JOHN, JOHNNY, KRISTELL, SEGOLENE", "CHRISTOPHE, JAMES, JIM, SANTIAGO, VALENTINE", "ANAELLE, ANNA, ANNABELLE, ANNE, ANNE LAURE, ANNICK, ANNIE, ANNY, ANOUK, NANCY", "LILIAN, LILIANE, LILIOSE, LILY, NATHALIE", "SAMSON", "MARTHE", "JULIETTE", "IGNACE"],
        ["ALPHONSE, ARCADIUS, ELEAZAR, ESPERANCE", "EUSEBE, JULIEN", "LYDIE", "JEAN-MARIE, VIANNEY", "ABEL, OSWALD", "MARLÈNE, OCTAVIEN", "GAETAN", "DOMINIQUE", "AMOUR", "ANNE LAURE, DIEUDONNE, LARRY, LAURA, LAURE, LAURENCE, LAURENE, LAURENT", "CLAIRE, GERIN, GILBERTE, SUZANNE, SUZETTE, SUZY", "CLARISSE", "HIPPOLYTE, ROSY", "ARNOLD, EVRARD", "ALFRED, ANNE-MARIE, MANON, MARIA, MARIE, MARIE-FRANCE, MARIE-LAURE, MARIELLE, MARILYNE, MARION, MARYLINE, MARYLISE, MARYSE, MARYVONNE, MIREILLE, MURIEL, MURIELLE, MYRIAM", "ARMEL, ARMELLE, ARZHEL, ARZHELA, ROCH", "HYACINTHE", "ELEN, HELENE, LAETITIA, LENA, MILENE, NELLEY", "JEAN EUDES, MYLÈNE", "BERNARD, PHILIBERT, SAM, SAMANTHA, SAMUEL, SAMY", "AHMED, CHRISTOFFE, CHRISTOPHE, GRACE, OMBELINE", "FABRICE, SIEGFRIED, SYMPHORIEN", "ROSA, ROSE, ROZEN, ROZENN", "BARTHELEMY", "CLOVIS, LOIC, LOUIS, LUDIVINE, LUDOVIC, MARCIEN", "CESAIRE, CESAR, NATACHA, NATASHA", "MONICA, MONIQUE", "AUGUSTIN, ELOUAN, LINDA, LOAN", "SABINE, SABRINA", "FIACRE, SABRINA, SACHA", "ARISTIDE"],
        ["GILLES, JOSUE", "INGRID", "GRAIG, GREGOIRE, GREGORY", "IRIS, IRMA, MOISE, MOSHE, ROSALIE", "RAISSA, RAÏSSA", "BERTRAND, EVA, EVELYNE", "REGINE, REINE, REJANE", "ADRIEN, ADRIENNE", "ALAIN", "AUBERT, INES", "ADELPHE, DAPHNÉE, GLEN, VINCIANE", "APOLLINAIRE, KIM", "AIME", "MATERNE", "DOLORES, LOLA, ROLAND", "CYPRIEN, EDITH, LUDMILLA, REGNAULT", "LAMBERT, REGINALD, RENALD, RENAUD, ROBERT, RONALD", "ARIANE, NADEGE, NADIA, NADINE, SONIA, VERA", "AMELIE, EMILIE, GABY", "DAVY", "DEBORAH, MATEO, MATHEO, MATHIEU, MATTEO, MATTHIEU", "MAURICE, MAURICETTE, MORVAN", "CONSTANT", "THECLE", "HERMANN", "COME, DAMIEN", "VINCENT DE PAUL", "VENCESLAS", "GABRIEL, MICHEL, MICHELE, MICHELLE, MICKAEL, MIKAEL, MIKAELA, RAPHAEL, RAPHAELE, RAPHAELLE", "GERONIMA, JEROME, NADIYE"],
        ["ARIELLE, EURIELLE, SULIAN, THERESE E.-J., URIELLE", "LEGER", "BLANCHE, CANDIDE, GERARD", "AURE, FRANCIS, FRANCK, FRANCOIS, ORIANNE, SOIZIC", "CAPUCINE, FLEUR, JASMINE, PLACIDE, VIOLAINE, VIOLETTE", "BRUNO", "GUSTAVE, SERGE", "EDUIN, MORGAN, PELAGIE, SAMARA", "DENIS, SARA, SARAH, SIBILLE", "GHISLAIN, GHISLAINE", "FIRMIN, SOLEDAD", "FRED, SERAPHINE, WILFRIED", "GERAUD", "CALLISTE, CELESTE, GWENDOLINE, JUSTE", "AURELIA, AURELIE, TERESA, THERESE D'AV.", "BERTRAND, EDWIGE, GAELA, GALL, PERLETTE", "BAUDOUIN, GAEL, GAELLE, SOLENE, SOLENNE", "AIMABLE, GENN, LUC, LUCA, LUCAS", "LAURYNE, RENE, RENEE", "ADELINE, ALINE, LINE", "CELINE, URSULA, URSULE", "ELODIE, SALOME", "JEAN DE CAPISTRAN", "FLORENTIN", "DARIA, DORIA, ENGUERRAN, ENGUERRAND, GOUENOU", "DIMITRI", "EMELINE", "JUDE, MONA, SIMON, SIMONE, THADDEE", "JORIS, NARCISSE", "BIENVENUE, HEIDI", "QUENTIN, WOLFGANG"],
        ["DREL, HAROLD, MATHURIN, TOUSSAIN", "DEFUNTS", "GWENAEL, GWENAELLE, HUBERT", "AYMERIC, CAROLINE, CHARLES, CHARLES BOR., CHARLY, JESSE, JESSICA, JESSIE, JESSY", "SYLVETTE, SYLVIANE, SYLVIE, ZAC, ZACHARIE", "BERTILLE, LEO, LEONARD, LEONARDO, WINNOC", "CAREN, CARINE, ERNEST, ERNIE, KARELLE, KAREN, KARINE", "CLAIR, GEOFFREY, GEOFFROY, GODEFROI, GODEFROY", "DORA, DORIAN, DORINE, THEO, THEODORE", "LEON, LEONTINE, LIONEL, NOE", "MARTIN", "CHRISTIAN, CHRISTIANE, EMILIEN", "BRICE, DIEGO, KILLIAN", "SIDOINE, SIDONIE", "ALBERT, ARTHUR, LEOPOLD, VICTOIRE, VICTORIA", "DAISY, GERTRUDE, MARGAUX, MARGOT, MARGUERITE, OMAR, OTMAR, PEGGY", "BABETTE, ELISABETH, ELISE, ELIZABETH, ELSA, HILDA, LESLIE, LISA, LISBETH, LISE", "AUDE", "TANGI, TANGUY", "EDMA, EDMEE, EDMOND, OCTAVE, OCTAVIE", "PERINNE, PERNELLE, PERRETTE, PERRINE, PETRONILLE, PIERRETTE", "CECILE, CELIA, SHEILA", "CLEMENT, CLEMENTINE, RACHILDE", "AUGUSTA, FLORA, FLORE", "CATHEL, CATHERINE, CATHY, KATEL, KATELL, KATIA, KATY", "CONRAD, DELPHINE, KURT", "ASTRID, SEVERIN, SEVERINE", "JACQUES DE LA M., ZDZISLAW, ZDZISLAWA", "SATURNIN", "ANDRE, ANDREA, ANDREE, ANDREV, TUGDUAL"],
        ["ALAN, ALAR, ALENA, ELOI, FLORENCE, NAHUM", "VIVIANE", "FRANCOIS-XAVIER, XAVIER", "ADA, ADNETTE, BARBAN, BARBARA, BARBE, BARBIE", "COLIN, GERALD, GERALDINE, NICOLETTA", "EVE, NICK, NICOLAS", "AMBROISE", "BUDOG, ELFRIED, FRIDA", "PIERRE FOURIER", "ROMA, ROMARIC", "DANIEL, DANIELE, DANIELLE, DANITZA, DANNY, DANY", "CHANTAL, CORENTIN, JEANNINE, JENNY, JUANITA, KRISTEN", "AURORE, JOCELYN, JOCELYNE, JOSSE, JOSSELIN, JOSSELINE, LUCETTE, LUCIE", "MELODIE, NOEMIE, ODILE", "CHRISTIANNE, NINON", "ADELAIDE, ALICE", "JUDICAEL, TESA, TESSA", "GATIEN, GRATIENNE, ROBIN", "URBAIN", "ABRAHAM, ISAAC, JACOB, JOY, THEOPHILE", "PIERRE C.", "FRANCOISE-XAVIERE, XAVIERE", "ARMAND, HARTMANN", "ADELE, DELPHIN, EUGENIE", "EMMANUEL, EMMANUELLE, LOLITA, MANUEL, NELLY, NOEL, NOELA, NOELLE", "ESTEBAN, ETIENNE, FANNY, STEFAN, STEPHANE, STEPHANIE, STEVE", "FABIOLA, HANS, YANNICK", "GASPARD", "DAVE, DAVID", "ROGER", "COLOMBE, SYLVESTRE"]];


});