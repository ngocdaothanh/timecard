module ApplicationHelper
  def current_user
    unless @current_user
      @current_user = User.find(session['user_id'])
    end
    @current_user
  end
  
  def isManagerOf(user)
    manager_id = User.find(user.id).manager_id

    if (manager_id.nil? || current_user.nil?)
      puts "(manager_id.nil? || current_user.nil?)"
      return false
    end
    current_user.id == manager_id
  end
end
