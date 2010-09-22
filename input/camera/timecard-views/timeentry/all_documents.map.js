function(doc) {
           if(doc.ruby_class && doc.ruby_class == 'Timeentry') {
             emit(doc['created_at'], 1);
           }
         }