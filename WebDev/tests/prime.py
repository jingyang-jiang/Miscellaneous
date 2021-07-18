from math import floor,sqrt
def is_prime(n):
    if n<2:
        return False
    for i in range(2,floor((sqrt(n))+1)):
        if n % i == 0 :
            return False
    return True