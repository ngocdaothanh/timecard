module ApplicationHelper
  def current_user
     puts "Trong ApplicationHelper current_user"
     if session['current_user'].nil?
       nil
     end
     session['current_user']
  end
end
