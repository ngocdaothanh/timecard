function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'User') {
             emit(doc['created_at'], 1);
           }
         }