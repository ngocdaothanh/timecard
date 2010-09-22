          function(doc) { 
            if (doc['ruby_class'] == 'Timeentry' && doc['user_id'] != null) {
              if (doc[''] && doc[''] != null){
                // "soft" deleted
              }else{
                emit([doc.user_id, doc.created_at], 1);
              }
            }
          }
