ConstraintDatabase resultCDB = resultCore.getContext();
ValueLookup valueLookup = resultCDB.getUnique(ValueLookup.class);
assertTrue(valueLookup.getEST(Term.createConstant("I1")) == 0);
assertTrue(valueLookup.getLST(Term.createConstant("I1")) == 70);
assertTrue(valueLookup.getEET(Term.createConstant("I1")) == 10);
assertTrue(valueLookup.getLET(Term.createConstant("I1")) == 80);
assertTrue(valueLookup.getEST(Term.createConstant("I2")) == 10);
assertTrue(valueLookup.getLST(Term.createConstant("I2")) == 80);
assertTrue(valueLookup.getEET(Term.createConstant("I2")) == 20);
assertTrue(valueLookup.getLET(Term.createConstant("I2")) == 90);
assertTrue(valueLookup.getEST(Term.createConstant("I3")) == 20);
assertTrue(valueLookup.getLST(Term.createConstant("I3")) == 90);
assertTrue(valueLookup.getEET(Term.createConstant("I3")) == 30);
assertTrue(valueLookup.getLET(Term.createConstant("I3")) == 100);
