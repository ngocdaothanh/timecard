function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'Tcexception') {
             emit(doc['created_at'], 1);
           }
         }