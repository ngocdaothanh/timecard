function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'Tcexception') {
             emit(doc['value'], 1);
           }
         }