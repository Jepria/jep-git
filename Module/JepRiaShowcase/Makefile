#
# Project Makefile
#

# Абстрактные цели
.PHONY:									\
	all										\
	$(MAKECMDGOALS)				\
	


#
# Транслируем цели в подкаталоги
#
all:
	@$(MAKE) -C DB $(MAKECMDGOALS)

ifeq ($(filter all,$(MAKECMDGOALS)),)
$(MAKECMDGOALS): all
endif
